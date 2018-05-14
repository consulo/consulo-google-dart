package com.jetbrains.lang.dart.ide.completion;

import static com.intellij.patterns.PlatformPatterns.psiElement;
import static com.intellij.patterns.StandardPatterns.or;

import gnu.trove.THashSet;

import java.util.Set;

import javax.annotation.Nonnull;
import com.intellij.codeInsight.completion.CompletionContributor;
import com.intellij.codeInsight.completion.CompletionParameters;
import consulo.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.completion.CompletionType;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.util.Condition;
import com.intellij.patterns.ElementPattern;
import com.intellij.patterns.PatternCondition;
import com.intellij.patterns.PsiElementPattern;
import com.intellij.psi.PsiElement;
import com.intellij.util.ProcessingContext;
import com.jetbrains.lang.dart.DartComponentType;
import com.jetbrains.lang.dart.ide.index.DartComponentInfo;
import com.jetbrains.lang.dart.psi.DartComponentName;
import com.jetbrains.lang.dart.psi.DartId;
import com.jetbrains.lang.dart.psi.DartNormalFormalParameter;
import com.jetbrains.lang.dart.psi.DartType;
import com.jetbrains.lang.dart.psi.DartVarAccessDeclaration;
import com.jetbrains.lang.dart.resolve.ClassNameScopeProcessor;
import com.jetbrains.lang.dart.util.DartResolveUtil;
import com.jetbrains.lang.dart.util.UsefulPsiTreeUtil;

public class DartClassNameCompletionContributor extends CompletionContributor
{
	public DartClassNameCompletionContributor()
	{
		final PsiElementPattern.Capture<PsiElement> idInComponentName = psiElement().withSuperParent(1, DartId.class).withSuperParent(2,
				DartComponentName.class);
		final ElementPattern<PsiElement> pattern = or(idInComponentName.withSuperParent(4, DartNormalFormalParameter.class),
				idInComponentName.withSuperParent(3, DartVarAccessDeclaration.class).with(new PatternCondition<PsiElement>("not after DartType")
		{
			public boolean accepts(@Nonnull final PsiElement element, final ProcessingContext context)
			{
				// no class name completion must be here: const type name<caret>;
				return !(UsefulPsiTreeUtil.getPrevSiblingSkipWhiteSpacesAndComments(element.getParent().getParent(), true) instanceof DartType);
			}
		}));

		extend(CompletionType.BASIC, pattern, new CompletionProvider()
		{
			@Override
			public void addCompletions(@Nonnull CompletionParameters parameters, ProcessingContext context, @Nonnull CompletionResultSet result)
			{
				final Set<DartComponentName> suggestedVariants = new THashSet<DartComponentName>();
				DartResolveUtil.treeWalkUpAndTopLevelDeclarations(parameters.getPosition(), new ClassNameScopeProcessor(suggestedVariants));

				for(DartComponentName variant : suggestedVariants)
				{
					result.addElement(LookupElementBuilder.create(variant));
				}
				if(parameters.getInvocationCount() > 1)
				{
					DartGlobalVariantsCompletionHelper.addAdditionalGlobalVariants(result, parameters.getPosition(), suggestedVariants,
							new Condition<DartComponentInfo>()
					{
						@Override
						public boolean value(DartComponentInfo info)
						{
							return info.getType() == DartComponentType.CLASS;
						}
					});
				}
			}
		});
	}
}
