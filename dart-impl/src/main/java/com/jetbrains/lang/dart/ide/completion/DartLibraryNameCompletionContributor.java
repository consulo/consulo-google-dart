package com.jetbrains.lang.dart.ide.completion;

import com.jetbrains.lang.dart.DartIcons;
import com.jetbrains.lang.dart.DartLanguage;
import com.jetbrains.lang.dart.ide.index.DartLibraryIndex;
import com.jetbrains.lang.dart.psi.DartId;
import com.jetbrains.lang.dart.psi.DartLibraryId;
import com.jetbrains.lang.dart.psi.DartPathOrLibraryReference;
import com.jetbrains.lang.dart.psi.DartStringLiteralExpression;
import consulo.annotation.component.ExtensionImpl;
import consulo.document.Document;
import consulo.language.Language;
import consulo.language.editor.completion.*;
import consulo.language.editor.completion.lookup.InsertionContext;
import consulo.language.editor.completion.lookup.LookupElement;
import consulo.language.editor.completion.lookup.LookupElementBuilder;
import consulo.language.editor.completion.lookup.LookupElementPresentation;
import consulo.language.util.ProcessingContext;
import consulo.project.Project;
import consulo.util.collection.ContainerUtil;

import jakarta.annotation.Nonnull;
import java.util.Set;

import static consulo.language.pattern.PlatformPatterns.psiElement;

@ExtensionImpl
public class DartLibraryNameCompletionContributor extends CompletionContributor {
  public DartLibraryNameCompletionContributor() {
    extend(CompletionType.BASIC,
           psiElement().withSuperParent(2, DartPathOrLibraryReference.class).withParent(DartStringLiteralExpression.class)
      ,
           new CompletionProvider() {
             @Override
             public void addCompletions(@Nonnull CompletionParameters parameters,
                                        ProcessingContext context,
                                        @Nonnull CompletionResultSet result) {
               final Project project = parameters.getPosition().getProject();
               final Set<String> names = DartLibraryIndex.getAllLibraryNames(project);
               names.addAll(ContainerUtil.map(DartLibraryIndex.getAllStandardLibrariesFromSdk(parameters.getPosition()),
                                              coreLib -> "dart:" + coreLib));
               names.add("package:");
               for (String libraryName : names) {
                 if (libraryName.endsWith(".dart")) {
                   continue;
                 }
                 result.addElement(new QuotedStringLookupElement(libraryName));
               }
             }
           });
    extend(CompletionType.BASIC, psiElement().withSuperParent(1, DartId.class).withSuperParent(2, DartLibraryId.class),
           new CompletionProvider() {
             @Override
             public void addCompletions(@Nonnull CompletionParameters parameters,
                                        ProcessingContext context,
                                        @Nonnull CompletionResultSet result) {
               for (String libraryName : DartLibraryIndex.getAllLibraryNames(parameters.getPosition().getProject())) {
                 result.addElement(LookupElementBuilder.create(libraryName));
               }
             }
           });
  }

  @Nonnull
  @Override
  public Language getLanguage() {
    return DartLanguage.INSTANCE;
  }

  public static class QuotedStringLookupElement extends LookupElement {
    private final String myName;

    public QuotedStringLookupElement(String name) {
      myName = name;
    }

    @Nonnull
    @Override
    public String getLookupString() {
      return myName;
    }

    @Override
    public void renderElement(LookupElementPresentation presentation) {
      super.renderElement(presentation);
      presentation.setIcon(DartIcons.Dart);
    }

    @Override
    public void handleInsert(InsertionContext context) {
      Document document = context.getDocument();
      int start = context.getStartOffset();
      int end = context.getTailOffset();
      if (start < 1 || end > document.getTextLength() - 1) {
        return;
      }
      CharSequence sequence = document.getCharsSequence();
      boolean left = sequence.charAt(start - 1) == sequence.charAt(start);
      boolean right = sequence.charAt(end - 1) == sequence.charAt(end);
      if (left || right) {
        document.replaceString(start, end, sequence.subSequence(left ? start + 1 : start, right ? end - 1 : end));
        if (right) {
          context.getEditor().getCaretModel().moveCaretRelatively(1, 0, false, false, true);
        }
      }
    }
  }
}