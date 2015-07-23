package com.labo.kaji.proxyservice.processor;

import com.labo.kaji.proxyservice.ProxyService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;

public class Processor extends AbstractProcessor {

    private Messager mMessager;
    private Filer mFiler;

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return new HashSet<String>() {{
            add(ProxyService.class.getCanonicalName());
        }};
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        mMessager = processingEnv.getMessager();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element annotatedElement : roundEnv.getElementsAnnotatedWith(ProxyService.class)) {
            // Make sure element is a field or a method declaration
            if (!annotatedElement.getKind().isClass()) {
                error(annotatedElement, "Only classes can be annotated with @%s", ProxyService.class.getSimpleName());
                return true;
            }

            try {
//                TypeSpec builderSpec = getBuilderSpec(annotatedElement);
//                JavaFile builderFile = JavaFile.builder(getPackageName(annotatedElement), builderSpec).build();
//                builderFile.writeTo(mFiler);
            } catch (Exception e) {
                error(annotatedElement, "Could not create proxy service for %s: %s", annotatedElement.getSimpleName(), e.getMessage());
            }
        }
        return true;
    }

    private void error(Element e, String msg, Object... args) {
        mMessager.printMessage(Diagnostic.Kind.ERROR, String.format(msg, args), e);
    }

    private String getPackageName(Element e) {
        while (!(e instanceof PackageElement)) {
            e = e.getEnclosingElement();
        }
        return ((PackageElement)e).getQualifiedName().toString();
    }

}
