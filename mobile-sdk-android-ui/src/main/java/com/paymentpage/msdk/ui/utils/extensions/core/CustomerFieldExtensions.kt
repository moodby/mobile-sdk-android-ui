package com.paymentpage.msdk.ui.utils.extensions.core

import com.paymentpage.msdk.core.domain.entities.customer.CustomerField
import com.paymentpage.msdk.core.domain.entities.customer.CustomerFieldValue
import com.paymentpage.msdk.ui.AdditionalField
import com.paymentpage.msdk.ui.PaymentActivity

internal fun CustomerField.validate(
    value: String,
    onTransformValueBeforeValidate: ((String) -> String)?
): String? {
    val validator = this.validator
    var resultMessage: String? = null
    if (this.isRequired && value.isEmpty()) {
        resultMessage =
            PaymentActivity.stringResourceManager.getStringByKey("message_required_field")
    } else if (validator != null) {
        val text = if (onTransformValueBeforeValidate != null)
            onTransformValueBeforeValidate(value)
        else
            value
        if (!validator.isValid(text))
            resultMessage = this.errorMessage ?: this.errorMessageKey
    }
    return resultMessage
}


internal fun List<CustomerField>.merge(
    changedFields: List<CustomerFieldValue>?,
    additionalFields: List<AdditionalField>
): List<CustomerFieldValue> {
    val result = changedFields?.toMutableList() ?: mutableListOf()
    this.forEach { field ->
        if (result.find { it.name == field.name } == null) {
            val foundAdditionalFieldValue = additionalFields.find { field.type == it.type }?.value
            result.add(CustomerFieldValue(field.name, foundAdditionalFieldValue ?: ""))
        }
    }

    return result.toList()
}

internal fun List<CustomerField>.visibleCustomerFields(): List<CustomerField> {
    return this.filter { !it.isHidden }
}

internal fun List<CustomerField>.isAllCustomerFieldsHidden(): Boolean {
    return !this.any { !it.isHidden }
}

internal fun List<CustomerField>.hasVisibleCustomerFields(): Boolean {
    return this.any { !it.isHidden }
}