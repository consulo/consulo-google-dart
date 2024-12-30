package com.jetbrains.lang.dart;

import consulo.annotation.component.ExtensionImpl;
import consulo.virtualFileSystem.fileType.FileTypeConsumer;
import consulo.virtualFileSystem.fileType.FileTypeFactory;

import jakarta.annotation.Nonnull;

/**
 * Created by IntelliJ IDEA.
 * User: Maxim.Mossienko
 * Date: 10/12/11
 * Time: 8:09 PM
 */
@ExtensionImpl
public class DartFileTypeFactory extends FileTypeFactory {
  @Override
  public void createFileTypes(@Nonnull FileTypeConsumer fileTypeConsumer) {
    fileTypeConsumer.consume(DartFileType.INSTANCE);
  }
}
