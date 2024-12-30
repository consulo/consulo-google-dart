package com.jetbrains.lang.dart.ide.completion;

import com.jetbrains.lang.dart.DartLanguage;
import com.jetbrains.lang.dart.DartTokenTypes;
import com.jetbrains.lang.dart.DartTokenTypesSets;
import com.jetbrains.lang.dart.psi.*;
import com.jetbrains.lang.dart.util.DartCodeGenerateUtil;
import com.jetbrains.lang.dart.util.UsefulPsiTreeUtil;
import consulo.annotation.component.ExtensionImpl;
import consulo.document.util.TextRange;
import consulo.language.Language;
import consulo.language.ast.IElementType;
import consulo.language.editor.completion.*;
import consulo.language.editor.completion.lookup.LookupElementBuilder;
import consulo.language.impl.ast.TreeUtil;
import consulo.language.impl.parser.GeneratedParserUtilBase;
import consulo.language.pattern.PsiElementPattern;
import consulo.language.pattern.StandardPatterns;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiFile;
import consulo.language.psi.PsiFileFactory;
import consulo.language.psi.util.PsiTreeUtil;
import consulo.language.util.ProcessingContext;
import consulo.util.lang.Pair;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import java.util.*;

import static consulo.language.pattern.PlatformPatterns.psiElement;

@ExtensionImpl
public class DartKeywordCompletionContributor extends CompletionContributor {
  private static final Set<String> allowedKeywords = new HashSet<String>() {
    {
      for (IElementType elementType : DartTokenTypesSets.RESERVED_WORDS.getTypes()) {
        add(elementType.toString());
      }
      for (IElementType elementType : DartTokenTypesSets.BUILT_IN_IDENTIFIERS.getTypes()) {
        add(elementType.toString());
      }
    }
  };

  public DartKeywordCompletionContributor() {
    final PsiElementPattern.Capture<PsiElement> idInExpression = psiElement().withSuperParent(1, DartId.class).withSuperParent(2,
                                                                                                                               DartReference.class);
    final PsiElementPattern.Capture<PsiElement> inComplexExpression = psiElement().withSuperParent(3, DartReference.class);
    final PsiElementPattern.Capture<PsiElement> inStringLiteral = psiElement().inside(DartStringLiteralExpression.class);
    final PsiElementPattern.Capture<PsiElement> inComment = psiElement().withElementType(DartTokenTypesSets.COMMENTS);

    final PsiElementPattern.Capture<PsiElement> elementCapture =
      psiElement().andNot(inComment).andNot(idInExpression.and(inComplexExpression))
                  .andNot(inStringLiteral);

    extend(CompletionType.BASIC, elementCapture, new CompletionProvider() {
      @Override
      public void addCompletions(@Nonnull CompletionParameters parameters, ProcessingContext context, @Nonnull CompletionResultSet result) {
        final Collection<String> suggestedKeywords = suggestKeywords(parameters.getPosition());
        suggestedKeywords.retainAll(allowedKeywords);
        for (String keyword : suggestedKeywords) {
          result.addElement(LookupElementBuilder.create(keyword));
        }
      }
    });
    extend(CompletionType.BASIC, psiElement().inFile(StandardPatterns.instanceOf(DartFile.class)).withParent(DartClassDefinition.class),
           new CompletionProvider() {
             @Override
             public void addCompletions(@Nonnull CompletionParameters parameters,
                                        ProcessingContext context,
                                        @Nonnull CompletionResultSet result) {
               result.addElement(LookupElementBuilder.create(DartTokenTypes.EXTENDS.toString()));
               result.addElement(LookupElementBuilder.create(DartTokenTypes.IMPLEMENTS.toString()));
             }
           });
  }

  private static Collection<String> suggestKeywords(PsiElement position) {
    final TextRange posRange = position.getTextRange();
    final PsiElement posFile = position.getContainingFile();

    final List<PsiElement> pathToBlockStatement = UsefulPsiTreeUtil.getPathToParentOfType(position, DartBlock.class);
    final DartPsiCompositeElement classInterface = PsiTreeUtil.getParentOfType(position, DartClassMembers.class);

    final String text;
    final int offset;
    if (pathToBlockStatement != null) {
      final Pair<String, Integer> pair = DartCodeGenerateUtil.wrapStatement(posRange.substring(posFile.getText()));
      text = pair.getFirst();
      offset = pair.getSecond();
    }
    else if (classInterface != null) {
      final Pair<String, Integer> pair = DartCodeGenerateUtil.wrapFunction(posRange.substring(posFile.getText()));
      text = pair.getFirst();
      offset = pair.getSecond();
    }
    else {
      DartEmbeddedContent embeddedContent = PsiTreeUtil.getParentOfType(position, DartEmbeddedContent.class);
      int startOffset = embeddedContent != null ? embeddedContent.getTextOffset() : 0;
      text = posRange.getStartOffset() == 0 ? "" : posFile.getText().substring(startOffset, posRange.getStartOffset());
      offset = 0;
    }

    final List<String> result = new ArrayList<String>();
    if (pathToBlockStatement != null && pathToBlockStatement.size() > 2) {
      final PsiElement blockChild = pathToBlockStatement.get(pathToBlockStatement.size() - 3);
      result.addAll(suggestBySibling(UsefulPsiTreeUtil.getPrevSiblingSkipWhiteSpacesAndComments(blockChild, true)));
    }

    final PsiFile file =
      PsiFileFactory.getInstance(posFile.getProject()).createFileFromText("a.dart", DartLanguage.INSTANCE, text, true, false);
    GeneratedParserUtilBase.CompletionState state = new GeneratedParserUtilBase.CompletionState(text.length() - offset);
    file.putUserData(GeneratedParserUtilBase.COMPLETION_STATE_KEY, state);
    TreeUtil.ensureParsed(file.getNode());
    result.addAll(state.items);
    return result;
  }

  @Nonnull
  private static Collection<? extends String> suggestBySibling(@Nullable PsiElement sibling) {
    if (DartIfStatement.class.isInstance(sibling)) {
      return Arrays.asList(DartTokenTypes.ELSE.toString());
    }
    else if (DartTryStatement.class.isInstance(sibling) || DartCatchPart.class.isInstance(sibling)) {
      return Arrays.asList(DartTokenTypes.CATCH.toString());
    }

    return Collections.emptyList();
  }

  @Nonnull
  @Override
  public Language getLanguage() {
    return DartLanguage.INSTANCE;
  }
}
