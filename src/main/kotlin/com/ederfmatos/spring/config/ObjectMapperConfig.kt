package com.ederfmatos.spring.config

import com.ederfmatos.spring.entities.Money
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.math.BigDecimal

@Configuration
class ObjectMapperConfig {

    class ValueObjectsModule : SimpleModule() {
        init {
            addSerializer(object : StdSerializer<Money>(Money::class.java) {
                override fun serialize(money: Money, generator: JsonGenerator, provider: SerializerProvider?) {
                    generator.writeNumber(money.value.setScale(2))
                }
            })
            addDeserializer(Money::class.java, object : StdDeserializer<Money>(Money::class.java) {
                override fun deserialize(parser: JsonParser, ctxt: DeserializationContext?): Money {
                    return Money(BigDecimal(parser.text))
                }
            })
        }
    }

    @Bean
    fun objectMapper(): ObjectMapper {
        val mapper: ObjectMapper = JsonMapper()
        mapper.registerModule(ValueObjectsModule())
        mapper.registerKotlinModule()
        mapper.registerModule(JavaTimeModule())
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL)
        mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        return mapper
    }

}