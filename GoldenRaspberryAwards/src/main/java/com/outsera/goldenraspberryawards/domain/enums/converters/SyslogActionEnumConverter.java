package com.outsera.goldenraspberryawards.domain.enums.converters;

import com.outsera.goldenraspberryawards.domain.enums.SyslogActionEnum;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class SyslogActionEnumConverter implements AttributeConverter<SyslogActionEnum, String> {

    @Override
    public String convertToDatabaseColumn(SyslogActionEnum status) {
        return (status == null) ? null : status.getMethod();
    }

    @Override
    public SyslogActionEnum convertToEntityAttribute(String method) {
        return SyslogActionEnum.toEnum(method);
    }

}
