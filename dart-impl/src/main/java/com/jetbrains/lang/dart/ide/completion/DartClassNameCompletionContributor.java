package com.jetbrains.lang.dart.ide.completion;

import com.jetbrains.lang.dart.DartComponentType;
import com.jetbrains.lang.dart.DartLanguage;
import com.jetbrains.lang.dart.ide.index.DartComponentInfo;
import com.jetbrains.lang.dart.psi.*;
import com.jetbrains.lang.dart.resolve.ClassNameScopeProcessor;
import com.jetbrains.lang.dart.util.DartResolveUtil;
import com.jetbrains.lang.dart.util.UsefulPsiTreeUtil;
import consulo.annotation.component.ExtensionImpl;
import consulo.language.Language;
import consulo.language.editor.completion.*;
import consulo.language.editor.completion.lookup.LookupElementBuilder;
import consulo.language.pattern.ElementPattern;
import consulo.language.pattern.PatternCondition;
import consulo.language.pattern.PsiElementPattern;
import consulo.language.psi.PsiElement;
import consulo.language.util.ProcessingContext;
import consulo.util.lang.function.Condition;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Set;

import static consulo.language.pattern.PlatformPatterns.psiElement;
import static consulo.language.pattern.StandardPatterns.or;

@ExtensionImpl
public class DartClassNameCompletionContributor extends CompletionContributor {
  public DartClassNameCompletionContributor() {
    final PsiElementPattern.Capture<PsiElement> idInComponentName = psiElement().withSuperParent(1, DartId.class).withSuperParent(2,
                                                                                                                                  DartComponentName.class);
    final ElementPattern<PsiElement> pattern = or(idInComponentName.withSuperParent(4, DartNormalFormalParameter.class),
                                                  idInComponentName.withSuperParent(3, DartVarAccessDeclaration.class)
                                                                   .with(new PatternCondition<PsiElement>("not after DartType") {
                                                                     public boolean accepts(@Nonnull final PsiElement element,
                                                                                            final ProcessingContext context) {
                                                                       // no class name completion must be here: const type name<caret>;
                                                                       return !(UsefulPsiTreeUtil.getPrevSiblingSkipWhiteSpacesAndComments(
                                                                         element.getParent().getParent(),
                                                                         true) instanceof DartType);
                                                                     }
                                                                   }));

    extend(CompletionType.BASIC, pattern, new CompletionProvider() {
      @Override
      public void addCompletions(@Nonnull CompletionParameters parameters, ProcessingContext context, @Nonnull CompletionResultSet result) {
        final Set<DartComponentName> suggestedVariants = new HashSet<DartComponentName>();
        DartResolveUtil.treeWalkUpAndTopLevelDeclarations(parameters.getPosition(), new ClassNameScopeProcessor(suggestedVariants));

        for (DartComponentName variant : suggestedVariants) {
          result.addElement(LookupElementBuilder.create(variant));
        }
        if (parameters.getInvocationCount() > 1) {
          DartGlobalVariantsCompletionHelper.addAdditionalGlobalVariants(result, parameters.getPosition(), suggestedVariants,
                                                                         new Condition<DartComponentInfo>() {
                                                                           @Override
                                                                           public boolean value(DartComponentInfo info) {
                                                                             return info.getType() == DartComponentType.CLASS;
                                                                           }
                                                                         });
        }
      }
    });
  }

  @Nonnull
  @Override
  public Language getLanguage() {
    return DartLanguage.INSTANCE;
  }
}
