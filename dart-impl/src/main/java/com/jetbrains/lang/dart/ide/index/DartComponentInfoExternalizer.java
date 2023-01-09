package com.jetbrains.lang.dart.ide.index;

import com.jetbrains.lang.dart.DartComponentType;
import consulo.index.io.data.DataExternalizer;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * @author: Fedor.Korotkov
 */
public class DartComponentInfoExternalizer implements DataExternalizer<DartComponentInfo> {
  @Override
  public void save(DataOutput out, DartComponentInfo componentInfo) throws IOException {
    out.writeUTF(componentInfo.getValue());
    final DartComponentType dartComponentType = componentInfo.getType();
    final int key = dartComponentType == null ? -1 : dartComponentType.getKey();
    out.writeInt(key);
    final String libraryId = componentInfo.getLibraryId();
    out.writeBoolean(libraryId != null);
    if (libraryId != null) {
      out.writeUTF(libraryId);
    }
  }

  @Override
  public DartComponentInfo read(DataInput in) throws IOException {
    final String value = in.readUTF();
    final int key = in.readInt();
    final boolean haveLibraryId = in.readBoolean();
    final String libraryId = haveLibraryId ? in.readUTF() : null;
    return new DartComponentInfo(value, DartComponentType.valueOf(key), libraryId);
  }
}
