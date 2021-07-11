package az.code.telegram_bot.configs;

import lombok.Getter;
import lombok.Setter;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "rabbit")
public class RabbitMQConfig {
    public final static String stop = "stop";
    public final static String receiver = "receiver";
    public final static String sender = "sender";
    public static String exchange;

    @Bean
    public Queue queueStop() {
        return new Queue(stop);
    }

    @Bean
    public Queue queueReceiver() {
        return new Queue(receiver);
    }

    @Bean
    public Queue queueSender() {
        return new Queue(sender);
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(exchange);
    }

    @Bean
    public Binding bindingReceiver(TopicExchange exchange) {
        return BindingBuilder.bind(queueReceiver()).to(exchange).with(queueReceiver().getName());
    }

    @Bean
    public Binding bindingStop(TopicExchange exchange) {
        return BindingBuilder.bind(queueStop()).to(exchange).with(queueStop().getName());
    }

    @Bean
    public Binding bindingSender(TopicExchange exchange) {
        return BindingBuilder.bind(queueSender()).to(exchange).with(queueSender().getName());
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
