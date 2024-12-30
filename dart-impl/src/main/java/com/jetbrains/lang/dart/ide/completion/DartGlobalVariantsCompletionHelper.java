package com.jetbrains.lang.dart.ide.completion;

import com.jetbrains.lang.dart.ide.index.DartComponentIndex;
import com.jetbrains.lang.dart.ide.index.DartComponentInfo;
import com.jetbrains.lang.dart.psi.DartComponentName;
import com.jetbrains.lang.dart.psi.DartReference;
import com.jetbrains.lang.dart.util.DartImportUtil;
import consulo.language.editor.completion.CompletionResultSet;
import consulo.language.editor.completion.lookup.InsertHandler;
import consulo.language.editor.completion.lookup.InsertionContext;
import consulo.language.editor.completion.lookup.LookupElement;
import consulo.language.editor.completion.lookup.LookupElementBuilder;
import consulo.language.psi.PsiElement;
import consulo.language.psi.util.PsiTreeUtil;
import consulo.util.collection.ContainerUtil;
import consulo.util.lang.function.Condition;
import consulo.util.lang.function.PairProcessor;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import java.util.List;
import java.util.Set;

/**
 * Created by fedorkorotkov.
 */
public class DartGlobalVariantsCompletionHelper {
  private DartGlobalVariantsCompletionHelper() {
  }

  public static void addAdditionalGlobalVariants(final CompletionResultSet result,
                                                 @Nonnull PsiElement context,
                                                 Set<DartComponentName> variants,
                                                 @Nullable final Condition<DartComponentInfo> infoFilter) {
    final List<String> addedNames = ContainerUtil.skipNulls(ContainerUtil.mapNotNull(variants, name -> name.getName()));
    DartComponentIndex.processAllComponents(context, new PairProcessor<String, DartComponentInfo>() {
                                              @Override
                                              public boolean process(String componentName, DartComponentInfo info) {
                                                if (infoFilter == null || !infoFilter.value(info)) {
                                                  result.addElement(buildElement(componentName, info));
                                                }
                                                return true;
                                              }
                                            }, new consulo.util.lang.function.Condition<String>() {
                                              @Override
                                              public boolean value(String componentName) {
                                                return addedNames.contains(componentName);
                                              }
                                            }
    );
  }

  @Nonnull
  private static LookupElement buildElement(String componentName, DartComponentInfo info) {
    LookupElementBuilder builder = LookupElementBuilder.create(info, componentName);
    if (info.getLibraryId() != null) {
      builder = builder.withTailText(info.getLibraryId(), true);
    }
    if (info.getType() != null) {
      builder = builder.withIcon(info.getType().getIcon());
    }
    return builder.withInsertHandler(MY_INSERT_HANDLER);
  }

  private static final InsertHandler<LookupElement> MY_INSERT_HANDLER = new InsertHandler<LookupElement>() {
    @Override
    public void handleInsert(InsertionContext context, LookupElement item) {
      DartComponentInfo info = (DartComponentInfo)item.getObject();
      final String libraryId = info.getLibraryId();
      if (libraryId == null) {
        return;
      }
      final PsiElement at = context.getFile().findElementAt(context.getStartOffset());
      final DartReference dartReference = PsiTreeUtil.getParentOfType(at, DartReference.class);
      if (dartReference != null && dartReference.resolve() == null) {
        DartImportUtil.insertImport(at.getContainingFile(), item.getLookupString(), libraryId);
      }
    }
  };
}
