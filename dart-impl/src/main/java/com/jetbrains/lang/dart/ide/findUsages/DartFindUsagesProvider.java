package com.jetbrains.lang.dart.ide.findUsages;

import com.jetbrains.lang.dart.DartComponentType;
import com.jetbrains.lang.dart.DartLanguage;
import consulo.annotation.component.ExtensionImpl;
import consulo.language.Language;
import consulo.language.findUsage.FindUsagesProvider;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiNamedElement;
import consulo.util.lang.StringUtil;

import jakarta.annotation.Nonnull;

/**
 * @author: Fedor.Korotkov
 */
@ExtensionImpl
public class DartFindUsagesProvider implements FindUsagesProvider {
  @Override
  public boolean canFindUsagesFor(@Nonnull PsiElement psiElement) {
    return psiElement instanceof PsiNamedElement;
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

  @Nonnull
  @Override
  public Language getLanguage() {
    return DartLanguage.INSTANCE;
  }
}
