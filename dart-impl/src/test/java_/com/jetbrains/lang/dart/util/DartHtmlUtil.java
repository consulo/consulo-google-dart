package com.jetbrains.lang.dart.util;

import consulo.language.psi.PsiFile;
import consulo.util.collection.ContainerUtil;
import consulo.virtualFileSystem.VirtualFile;
import com.intellij.testFramework.fixtures.CodeInsightTestFixture;
import consulo.ide.impl.idea.util.Function;
import consulo.util.io.StreamUtil;

import java.io.IOException;
import java.util.List;

public class DartHtmlUtil {
  public static String getHtmlText(VirtualFile root) throws IOException {
    String text = new String(StreamUtil.loadFromStream(root.getInputStream()));
    return getHtmlText(text);
  }

  public static String getHtmlText(String text) {
    return "<html>\n" +
           "  <body>\n" +
           "    <script type=\"application/dart\">\n" +
           text +
           "    </script>\n" +
           "  </body>\n" +
           "</html>";
  }

  public static PsiFile createHtmlAndConfigureFixture(final CodeInsightTestFixture myFixture, String... fileNames) throws IOException {
    List<VirtualFile> virtualFileList = ContainerUtil.map(fileNames, new Function<String, VirtualFile>() {
      @Override
      public VirtualFile fun(String name) {
        return myFixture.copyFileToProject(name.endsWith(".dart") ? name : name + ".dart");
      }
    });
    VirtualFile root = virtualFileList.iterator().next();
    return myFixture.configureByText(fileNames[0] + ".html", DartHtmlUtil.getHtmlText(root));
  }
}
