package com.jetbrains.lang.dart.ide.findUsages;

import javax.annotation.Nonnull;

import com.intellij.lang.cacheBuilder.WordsScanner;
import com.intellij.lang.findUsages.FindUsagesProvider;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.jetbrains.lang.dart.DartComponentType;

/**
 * @author: Fedor.Korotkov
 */
public class DartFindUsagesProvider implements FindUsagesProvider {
  @Override
  public WordsScanner getWordsScanner() {
    return null;
  }

  @Override
  public boolean canFindUsagesFor(@Nonnull PsiElement psiElement) {
    return psiElement instanceof PsiNamedElement;
  }

  @Override
  public String getHelpId(@Nonnull PsiElement psiElement) {
    return null;
  }

  @Nonnull
  public String getType(@Nonnull final PsiElement element) {
    final DartComponentType type = DartComponentType.typeOf(element.getParent());
    return type == null ? "reference" : type.toString().toLowerCase();
  }

  @Nonnull
  public String getDescriptiveName(@Nonnull final PsiElement element) {
    if (element instanceof PsiNamedElement) {
      return StringUtil.notNullize(((PsiNamedElement)element).getName());
    }
    return "";
  }

  @Nonnull
  public String getNodeText(@Nonnull final PsiElement element, final boolean useFullName) {
    final String name = element instanceof PsiNamedElement ? ((PsiNamedElement)element).getName() : null;
    return name != null ? name : element.getText();
  }
}
