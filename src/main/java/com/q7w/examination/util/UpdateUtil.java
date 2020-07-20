package com.q7w.examination.util;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;


import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class UpdateUtil {
    /**
     * 所有为空值的属性都不copy
     * @param source
     * @param target
     */
    public static void copyNullProperties(Object source, Object target) {
        org.springframework.beans.BeanUtils.copyProperties(source, target, getNullField(source));
    }

    /**
     * 获取属性中为空的字段
     *
     * @param target
     * @return
     */
    private static String[] getNullField(Object target) {
        BeanWrapper beanWrapper = new BeanWrapperImpl(target);
        PropertyDescriptor[] propertyDescriptors = beanWrapper.getPropertyDescriptors();
        Set<String> notNullFieldSet = new HashSet<>();
        if (propertyDescriptors.length > 0) {
            for (PropertyDescriptor p : propertyDescriptors) {
                String name = p.getName();
                Object value = beanWrapper.getPropertyValue(name);
                if (Objects.isNull(value)) {
                    notNullFieldSet.add(name);
                }
            }
        }
        String[] notNullField = new String[notNullFieldSet.size()];
        return notNullFieldSet.toArray(notNullField);
    }

}
