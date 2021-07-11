package az.code.telegram_bot.configs;

import lombok.Getter;
import lombok.Setter;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Setter
@Getter
@Configuration
//@ConfigurationProperties(prefix = "rabbit")
public class RabbitMQConfig {
    public final static String cancelled = "cancelled";
    public final static String offered = "offered";
    public final static String sended = "sended";
    public final static String accepted = "accepted";
    public final static String exchange = "exchange";

    @Bean
    public Queue queueCancelled() {
        return new Queue(cancelled);
    }

    @Bean
    public Queue queueOffered() {
        return new Queue(offered);
    }

    @Bean
    public Queue queueSended() {
        return new Queue(sended);
    }

    @Bean
    public Queue queueAccepted() {
        return new Queue(accepted);
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(exchange);
    }

    @Bean
    public Binding bindingOffered(TopicExchange exchange) {
        return BindingBuilder.bind(queueOffered()).to(exchange).with(queueOffered().getName());
    }

    @Bean
    public Binding bindingCancelled(TopicExchange exchange) {
        return BindingBuilder.bind(queueCancelled()).to(exchange).with(queueCancelled().getName());
    }

    @Bean
    public Binding bindingSended(TopicExchange exchange) {
        return BindingBuilder.bind(queueSended()).to(exchange).with(queueSended().getName());
    }

    @Bean
    public Binding bindingAccepted(TopicExchange exchange) {
        return BindingBuilder.bind(queueAccepted()).to(exchange).with(queueAccepted().getName());
    }

    @Bean
    public MessageConverter converter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate template(ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(converter());
        return rabbitTemplate;
    }

}
