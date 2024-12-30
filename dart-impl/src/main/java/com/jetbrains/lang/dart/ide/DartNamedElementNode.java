package com.jetbrains.lang.dart.ide;

import com.jetbrains.lang.dart.psi.DartClass;
import com.jetbrains.lang.dart.psi.DartComponent;
import consulo.component.util.Iconable;
import consulo.language.editor.generation.ClassMember;
import consulo.language.editor.generation.MemberChooserObject;
import consulo.language.editor.generation.PsiElementMemberChooserObject;
import consulo.language.icon.IconDescriptorUpdaters;
import consulo.language.psi.util.PsiTreeUtil;
import consulo.navigation.ItemPresentation;

import jakarta.annotation.Nullable;

/**
 * @author: Fedor.Korotkov
 */
public class DartNamedElementNode extends PsiElementMemberChooserObject implements ClassMember {
  public DartNamedElementNode(final DartComponent haxeNamedComponent) {
    super(haxeNamedComponent,
          buildPresentationText(haxeNamedComponent),
          IconDescriptorUpdaters.getIcon(haxeNamedComponent, Iconable.ICON_FLAG_VISIBILITY));
  }

  @Nullable
  private static String buildPresentationText(DartComponent haxeNamedComponent) {
    final ItemPresentation presentation = haxeNamedComponent.getPresentation();
    if (presentation == null) {
      return haxeNamedComponent.getName();
    }
    final StringBuilder result = new StringBuilder();
    if (haxeNamedComponent instanceof DartClass) {
      result.append(haxeNamedComponent.getName());
      final String location = presentation.getLocationString();
      if (location != null && !location.isEmpty()) {
        result.append(" ").append(location);
      }
    }
    else {
      result.append(presentation.getPresentableText());
    }
    return result.toString();
  }

  @Nullable
  @Override
  public MemberChooserObject getParentNodeDelegate() {
    final DartComponent result = PsiTreeUtil.getParentOfType(getPsiElement(), DartComponent.class);
    return result == null ? null : new DartNamedElementNode(result);
  }
}
