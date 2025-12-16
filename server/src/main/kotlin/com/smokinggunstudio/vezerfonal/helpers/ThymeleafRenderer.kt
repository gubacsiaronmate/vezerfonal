package com.smokinggunstudio.vezerfonal.helpers

import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context
import org.thymeleaf.templatemode.TemplateMode
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver

class ThymeleafRenderer {
    private val templateEngine = TemplateEngine().apply {
        setTemplateResolver(
            ClassLoaderTemplateResolver().apply {
                prefix = "templates/"
                suffix = ".html"
                characterEncoding = "utf-8"
                templateMode = TemplateMode.HTML
                isCacheable = true
            }
        )
    }
    
    fun render(
        templateName: String,
        variables: Map<String, Any>,
    ): String {
        val context = Context()
        variables.forEach { (name, value) ->
            context.setVariable(name, value)
        }
        return templateEngine.process(templateName, context)
    }
}