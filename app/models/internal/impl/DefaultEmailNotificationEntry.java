/*
 * Copyright 2017 Smartsheet
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package models.internal.impl;

import com.arpnetworking.commons.builder.OvalBuilder;
import com.arpnetworking.logback.annotations.Loggable;
import com.arpnetworking.steno.Logger;
import com.arpnetworking.steno.LoggerFactory;
import com.google.common.collect.ImmutableMap;
import com.google.inject.Injector;
import com.typesafe.config.Config;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import models.internal.Alert;
import models.internal.AlertTrigger;
import models.internal.NotificationEntry;
import net.sf.oval.constraint.NotEmpty;
import net.sf.oval.constraint.NotNull;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import java.io.IOException;
import java.io.StringWriter;
import java.net.URI;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * Represents a notification by email.
 *
 * @author Brandon Arp (brandon dot arp at smartsheet dot com)
 */
@Loggable
public final class DefaultEmailNotificationEntry implements NotificationEntry {

    @Override
    @SuppressFBWarnings(value = "NP_NONNULL_PARAM_VIOLATION",
            justification = "Bug in findbugs: https://github.com/findbugsproject/findbugs/issues/79")
    public CompletionStage<Void> notifyRecipient(final Alert alert, final AlertTrigger trigger, final Injector injector) {
        LOGGER.debug()
                .setMessage("Sending email notification")
                .addData("address", _address)
                .addData("trigger", trigger)
                .log();
        final Configuration configuration = injector.getInstance(Configuration.class);
        final Config typesafeConfig = injector.getInstance(Config.class);

        // Markdown rendering
        final Parser parser = injector.getInstance(Parser.class);
        final HtmlRenderer renderer = injector.getInstance(HtmlRenderer.class);
        try {
            final Session mailSession = injector.getInstance(Session.class);
            final MimeMessage mailMessage = new MimeMessage(mailSession);
            mailMessage.addRecipients(Message.RecipientType.TO, _address);
            mailMessage.setFrom(typesafeConfig.getString("alerts.email.from"));
            final String baseUrl = typesafeConfig.getString("alerts.baseUrl");
            final String alertUrl = URI.create(baseUrl).resolve("/#alert/edit/" + alert.getId()).toString();
            final String subject = String.format("Alert '%s' on %s in alarm ", alert.getName(), getGroupByString(trigger));
            mailMessage.setSubject(subject);
            final MimeMultipart multipart = new MimeMultipart("alternative");
            final ImmutableMap<String, Object> templateObject = ImmutableMap.<String, Object>builder()
                    .put("alert", alert)
                    .put("htmlComment", renderer.render(parser.parse(alert.getComment())))
                    .put("trigger", trigger)
                    .put("alertUrl", alertUrl)
                    .put("groupBy", getGroupByString(trigger))
                    .build();

            addBodyPart(multipart, "text/plain; charset=utf-8", renderTemplate(templateObject, configuration, "alert.text.ftl"));
            addBodyPart(multipart, "text/html; charset=utf-8", renderTemplate(templateObject, configuration, "alert.html.ftlh"));

            if (multipart.getCount() == 0) {
                final String text = "Alert '" + alert.getName() + "' on " + getGroupByString(trigger) + " has gone into alert: \n"
                        + "Details: " + trigger.getArgs().toString() + "\n";
                final MimeBodyPart bodyText = new MimeBodyPart();
                bodyText.setText(text, "utf-8");
                multipart.addBodyPart(bodyText);
            }

            mailMessage.setContent(multipart);
            Transport.send(mailMessage);
            return CompletableFuture.completedFuture(null);
            // CHECKSTYLE.OFF: IllegalCatch - Make sure that template execution gets reported properly
        } catch (final RuntimeException | MessagingException e) {
            // CHECKSTYLE.ON: IllegalCatch
            final CompletableFuture<Void> future = new CompletableFuture<>();
            future.completeExceptionally(e);
            return future;
        }
    }

    private String getGroupByString(final AlertTrigger trigger) {
        return trigger.getGroupBy()
                .entrySet()
                .stream()
                .map(entry -> String.format("%s %s", entry.getKey(), entry.getValue()))
                .collect(Collectors.joining(", "));
    }

    private String renderTemplate(final ImmutableMap<String, Object> templateObject,
            final Configuration configuration,
            final String templateName) {
        try {
            final StringWriter stringWriter = new StringWriter();
            final Template template = configuration.getTemplate(templateName);
            template.process(templateObject, stringWriter);
            return stringWriter.toString();
        } catch (final IOException e) {
            LOGGER.warn()
                    .setMessage("Error loading alert template")
                    .setThrowable(e)
                    .addData("template", templateName)
                    .log();
            throw new RuntimeException("error processing template " + templateName, e);
        } catch (final TemplateException e) {
            LOGGER.warn()
                    .setMessage("Error processing alert template")
                    .addData("template", templateName)
                    .setThrowable(e)
                    .log();
            throw new RuntimeException("error processing template " + templateName, e);
        }
    }

    private void addBodyPart(
            final MimeMultipart multipart, final String contentType, final String text)
            throws MessagingException {
            final MimeBodyPart part = new MimeBodyPart();
            part.setContent(text, contentType);
            multipart.addBodyPart(part);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_address);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final DefaultEmailNotificationEntry other = (DefaultEmailNotificationEntry) o;
        return Objects.equals(_address, other._address);
    }

    public String getAddress() {
        return _address;
    }

    private DefaultEmailNotificationEntry(final Builder builder) {
        _address = builder._address;
    }

    private final String _address;

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultEmailNotificationEntry.class);

    /**
     * Implementation of the builder pattern for a {@link DefaultEmailNotificationEntry}.
     *
     * @author Brandon Arp (brandon dot arp at smartsheet dot com)
     */
    public static final class Builder extends OvalBuilder<DefaultEmailNotificationEntry> {
        /**
         * Public constructor.
         */
        public Builder() {
            super(DefaultEmailNotificationEntry::new);
        }

        /**
         * The email address. Required. Cannot be null or empty.
         *
         * @param value The email address.
         * @return This instance of {@link Builder}.
         */
        public Builder setAddress(final String value) {
            _address = value;
            return this;
        }

        @NotNull
        @NotEmpty
        private String _address;
    }
}
