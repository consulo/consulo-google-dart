package com.jetbrains.lang.dart.psi.impl;

import com.jetbrains.lang.dart.psi.DartPathOrLibraryReference;
import consulo.annotation.component.ExtensionImpl;
import consulo.document.util.TextRange;
import consulo.language.psi.ElementManipulator;
import consulo.language.util.IncorrectOperationException;

import javax.annotation.Nonnull;

@ExtensionImpl
public class DartPathOrLibraryManipulator implements ElementManipulator<DartPathOrLibraryReference> {
  @Override
  public DartPathOrLibraryReference handleContentChange(@Nonnull DartPathOrLibraryReference element, @Nonnull TextRange range,
                                                        String newContent) throws IncorrectOperationException {
    return element;
  }

  @Override
  public DartPathOrLibraryReference handleContentChange(@Nonnull DartPathOrLibraryReference element,
                                                        String newContent) throws IncorrectOperationException {
    return element;
  }

  @Nonnull
  @Override
  public TextRange getRangeInElement(@Nonnull DartPathOrLibraryReference element) {
    return element.getTextRange();
  }

  @Nonnull
  @Override
  public Class<DartPathOrLibraryReference> getElementClass() {
    return DartPathOrLibraryReference.class;
  }
}
