package com.example.compilerlib;

import com.example.annotationlib.DIActivity;
import com.example.annotationlib.DIView;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.lang.annotation.ElementType;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

@AutoService(Processor.class)
public class DIProcessor extends AbstractProcessor {

    private Elements mElementUtils;

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Collections.singleton(DIActivity.class.getCanonicalName());
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(DIActivity.class);
        for (Element element : elements) {
            // TODO: 2019/1/30 is class?
            TypeElement typeElement = (TypeElement) element;

            MethodSpec.Builder bindView = MethodSpec.methodBuilder("bindView")
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                    .returns(TypeName.VOID)
                    // TODO: 2019/1/30 type name 的获取有必要这样吗
                    .addParameter(ClassName.get(typeElement.asType()), "activity");

            List<? extends Element> allMembers = mElementUtils.getAllMembers(typeElement);
            for (Element member : allMembers) {
                DIView diView = member.getAnnotation(DIView.class);
                if (diView == null) continue;
                bindView.addStatement(String.format("activity.%s = (%s)activity.findViewById(%s)",
                        member.getSimpleName(), ClassName.get(member.asType()).toString(), diView.value()));
            }
            TypeSpec clazz = TypeSpec.classBuilder("DI" + typeElement.getSimpleName())
                    .superclass(TypeName.get(typeElement.asType()))
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                    .addMethod(bindView.build())
                    .build();

            JavaFile javaFile = JavaFile.builder(mElementUtils.getPackageOf(typeElement).getQualifiedName().toString(),
                    clazz).build();

            try {
                javaFile.writeTo(processingEnv.getFiler());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        mElementUtils = processingEnvironment.getElementUtils();
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.RELEASE_7;
    }
}
