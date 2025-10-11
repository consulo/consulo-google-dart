package com.jetbrains.lang.dart.ide.refactoring.introduce;

import com.jetbrains.lang.dart.DartComponentType;
import com.jetbrains.lang.dart.DartTokenTypes;
import com.jetbrains.lang.dart.psi.*;
import com.jetbrains.lang.dart.util.DartElementGenerator;
import com.jetbrains.lang.dart.util.DartNameSuggesterUtil;
import com.jetbrains.lang.dart.util.DartRefactoringUtil;
import com.jetbrains.lang.dart.util.UsefulPsiTreeUtil;
import consulo.application.ApplicationManager;
import consulo.application.Result;
import consulo.codeEditor.CaretModel;
import consulo.codeEditor.Editor;
import consulo.codeEditor.SelectionModel;
import consulo.dataContext.DataContext;
import consulo.document.Document;
import consulo.google.dart.localize.DartLocalize;
import consulo.language.ast.ASTNode;
import consulo.language.editor.CodeInsightUtilCore;
import consulo.language.editor.WriteCommandAction;
import consulo.language.editor.refactoring.RefactoringBundle;
import consulo.language.editor.refactoring.action.RefactoringActionHandler;
import consulo.language.editor.refactoring.introduce.IntroduceTargetChooser;
import consulo.language.editor.refactoring.introduce.inplace.InplaceVariableIntroducer;
import consulo.language.editor.refactoring.introduce.inplace.OccurrencesChooser;
import consulo.language.editor.refactoring.util.CommonRefactoringUtil;
import consulo.language.editor.template.TemplateManager;
import consulo.language.editor.template.TemplateState;
import consulo.language.psi.*;
import consulo.language.psi.util.PsiTreeUtil;
import consulo.localize.LocalizeValue;
import consulo.project.Project;
import consulo.ui.annotation.RequiredUIAccess;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.*;

@SuppressWarnings("MethodMayBeStatic")
public abstract class DartIntroduceHandler implements RefactoringActionHandler {
    @Nullable
    protected static PsiElement findAnchor(PsiElement occurrence) {
        return findAnchor(Arrays.asList(occurrence));
    }

    @Nullable
    protected static PsiElement findAnchor(List<PsiElement> occurrences) {
        int minOffset = Integer.MAX_VALUE;
        for (PsiElement element : occurrences) {
            minOffset = Math.min(minOffset, element.getTextOffset());
        }

        DartStatements statements = findContainingStatements(occurrences);
        if (statements == null) {
            return null;
        }

        PsiElement child = null;
        PsiElement[] children = statements.getChildren();
        for (PsiElement aChildren : children) {
            child = aChildren;
            if (child.getTextRange().contains(minOffset)) {
                break;
            }
        }

        return child;
    }

    @Nullable
    private static DartStatements findContainingStatements(List<PsiElement> occurrences) {
        DartStatements result = PsiTreeUtil.getParentOfType(occurrences.get(0), DartStatements.class, true);
        while (result != null && !UsefulPsiTreeUtil.isAncestor(result, occurrences, true)) {
            result = PsiTreeUtil.getParentOfType(result, DartStatements.class, true);
        }
        return result;
    }

    protected final LocalizeValue myDialogTitle;

    public DartIntroduceHandler(@Nonnull final LocalizeValue dialogTitle) {
        myDialogTitle = dialogTitle;
    }

    @Override
    public void invoke(@Nonnull Project project, Editor editor, PsiFile file, DataContext dataContext) {
        performAction(new DartIntroduceOperation(project, editor, file, null));
    }

    @Override
    public void invoke(@Nonnull Project project, @Nonnull PsiElement[] elements, DataContext dataContext) {
    }

    public void performAction(DartIntroduceOperation operation) {
        final PsiFile file = operation.getFile();
        if (!CommonRefactoringUtil.checkReadOnlyStatus(file)) {
            return;
        }
        final Editor editor = operation.getEditor();
        if (editor.getSettings().isVariableInplaceRenameEnabled()) {
            final TemplateState templateState =
                TemplateManager.getInstance(operation.getProject()).getTemplateState(operation.getEditor());
            if (templateState != null && !templateState.isFinished()) {
                return;
            }
        }

        PsiElement element1 = null;
        PsiElement element2 = null;
        final SelectionModel selectionModel = editor.getSelectionModel();
        if (selectionModel.hasSelection()) {
            element1 = file.findElementAt(selectionModel.getSelectionStart());
            element2 = file.findElementAt(selectionModel.getSelectionEnd() - 1);
            if (element1 instanceof PsiWhiteSpace) {
                int startOffset = element1.getTextRange().getEndOffset();
                element1 = file.findElementAt(startOffset);
            }
            if (element2 instanceof PsiWhiteSpace) {
                int endOffset = element2.getTextRange().getStartOffset();
                element2 = file.findElementAt(endOffset - 1);
            }
        }
        else {
            if (smartIntroduce(operation)) {
                return;
            }
            final CaretModel caretModel = editor.getCaretModel();
            final Document document = editor.getDocument();
            int lineNumber = document.getLineNumber(caretModel.getOffset());
            if ((lineNumber >= 0) && (lineNumber < document.getLineCount())) {
                element1 = file.findElementAt(document.getLineStartOffset(lineNumber));
                element2 = file.findElementAt(document.getLineEndOffset(lineNumber) - 1);
            }
        }
        final Project project = operation.getProject();
        if (element1 == null || element2 == null) {
            showCannotPerformError(project, editor);
            return;
        }

        element1 = DartRefactoringUtil.getSelectedExpression(project, file, element1, element2);
        if (element1 == null) {
            showCannotPerformError(project, editor);
            return;
        }

        if (!checkIntroduceContext(file, editor, element1)) {
            return;
        }
        operation.setElement(element1);
        performActionOnElement(operation);
    }

    protected boolean checkIntroduceContext(PsiFile file, Editor editor, PsiElement element) {
        if (!isValidIntroduceContext(element)) {
            showCannotPerformError(file.getProject(), editor);
            return false;
        }
        return true;
    }

    @RequiredUIAccess
    private void showCannotPerformError(Project project, Editor editor) {
        CommonRefactoringUtil.showErrorHint(project, editor, DartLocalize.refactoringIntroduceSelectionError().get(), myDialogTitle.get(),
            "refactoring.extractMethod");
    }

    protected boolean isValidIntroduceContext(PsiElement element) {
        return PsiTreeUtil.getParentOfType(element, DartFormalParameterList.class) == null;
    }

    private boolean smartIntroduce(final DartIntroduceOperation operation) {
        final Editor editor = operation.getEditor();
        final PsiFile file = operation.getFile();
        int offset = editor.getCaretModel().getOffset();
        PsiElement elementAtCaret = file.findElementAt(offset);
        if (!checkIntroduceContext(file, editor, elementAtCaret)) {
            return true;
        }
        final List<DartExpression> expressions = new ArrayList<DartExpression>();
        while (elementAtCaret != null) {
            if (elementAtCaret instanceof DartFile) {
                break;
            }
            if (elementAtCaret instanceof DartExpression) {
                expressions.add((DartExpression) elementAtCaret);
            }
            elementAtCaret = elementAtCaret.getParent();
        }
        if (expressions.size() == 1 || ApplicationManager.getApplication().isUnitTestMode()) {
            operation.setElement(expressions.get(0));
            performActionOnElement(operation);
            return true;
        }
        else if (expressions.size() > 1) {
            IntroduceTargetChooser.showChooser(editor, expressions, expression -> {
                    operation.setElement(expression);
                    performActionOnElement(operation);
                }, expression -> expression.getText()
            );
            return true;
        }
        return false;
    }

    private void performActionOnElement(DartIntroduceOperation operation) {
        if (!checkEnabled(operation)) {
            return;
        }
        final PsiElement element = operation.getElement();

        final DartExpression initializer = (DartExpression) element;
        operation.setInitializer(initializer);

        operation.setOccurrences(getOccurrences(element, initializer));
        operation.setSuggestedNames(DartNameSuggesterUtil.getSuggestedNames(initializer));
        if (operation.getOccurrences().size() == 0) {
            operation.setReplaceAll(false);
        }

        performActionOnElementOccurrences(operation);
    }

    protected void performActionOnElementOccurrences(final DartIntroduceOperation operation) {
        final Editor editor = operation.getEditor();
        if (editor.getSettings().isVariableInplaceRenameEnabled()) {
            ensureName(operation);
            if (operation.isReplaceAll()) {
                performInplaceIntroduce(operation);
            }
            else {
                OccurrencesChooser.simpleChooser(editor).showChooser(operation.getElement(), operation.getOccurrences(),
                    replaceChoice -> {
                        operation.setReplaceAll(replaceChoice == OccurrencesChooser.ReplaceChoice.ALL);
                        performInplaceIntroduce(operation);
                    });
            }
        }
        else {
            performIntroduceWithDialog(operation);
        }
    }

    protected boolean checkEnabled(DartIntroduceOperation operation) {
        return true;
    }

    protected static void ensureName(DartIntroduceOperation operation) {
        if (operation.getName() == null) {
            final Collection<String> suggestedNames = operation.getSuggestedNames();
            if (suggestedNames.size() > 0) {
                operation.setName(suggestedNames.iterator().next());
            }
            else {
                operation.setName("x");
            }
        }
    }


    protected List<PsiElement> getOccurrences(PsiElement element, @Nonnull final DartExpression expression) {
        PsiElement context = element;
        DartComponentType type = null;
        do {
            context = PsiTreeUtil.getParentOfType(context, DartComponent.class, true);
            type = DartComponentType.typeOf(context);
        }
        while (type != null && notFunctionMethodClass(type));
        if (context == null) {
            context = expression.getContainingFile();
        }
        return DartRefactoringUtil.getOccurrences(expression, context);
    }

    private static boolean notFunctionMethodClass(DartComponentType type) {
        final boolean isFunctionMethodClass = type == DartComponentType.METHOD ||
            type == DartComponentType.FUNCTION ||
            type == DartComponentType.CLASS;
        return !isFunctionMethodClass;
    }

    protected void performIntroduceWithDialog(DartIntroduceOperation operation) {
        final Project project = operation.getProject();
        if (operation.getName() == null) {
            DartIntroduceDialog dialog = new DartIntroduceDialog(project, myDialogTitle, operation);
            dialog.show();
            if (!dialog.isOK()) {
                return;
            }
            operation.setName(dialog.getName());
            operation.setReplaceAll(dialog.doReplaceAllOccurrences());
        }

        PsiElement declaration = performRefactoring(operation);
        if (declaration == null) {
            return;
        }
        final Editor editor = operation.getEditor();
        editor.getCaretModel().moveToOffset(declaration.getTextRange().getEndOffset());
        editor.getSelectionModel().removeSelection();
    }

    protected void performInplaceIntroduce(DartIntroduceOperation operation) {
        final PsiElement statement = performRefactoring(operation);
        final DartComponent target = PsiTreeUtil.findChildOfType(statement, DartComponent.class);
        final DartComponentName componentName = target != null ? target.getComponentName() : null;
        if (componentName == null) {
            return;
        }
        final List<PsiElement> occurrences = operation.getOccurrences();
        operation.getEditor().getCaretModel().moveToOffset(componentName.getTextOffset());
        final InplaceVariableIntroducer<PsiElement> introducer = new DartInplaceVariableIntroducer(componentName, operation, occurrences);
        introducer.performInplaceRefactoring(new LinkedHashSet<String>(operation.getSuggestedNames()));
    }

    @Nullable
    @RequiredUIAccess
    protected PsiElement performRefactoring(@Nonnull DartIntroduceOperation operation) {
        PsiElement anchor = operation.isReplaceAll() ? findAnchor(operation.getOccurrences()) : findAnchor(operation.getInitializer());
        if (anchor == null) {
            CommonRefactoringUtil.showErrorHint(operation.getProject(),
                operation.getEditor(),
                RefactoringBundle.getCannotRefactorMessage(DartLocalize.dartRefactoringIntroduceAnchorError().get()),
                DartLocalize.dartRefactoringIntroduceError().get(),
                null);
            return null;
        }
        PsiElement declaration = createDeclaration(operation);
        if (declaration == null) {
            showCannotPerformError(operation.getProject(), operation.getEditor());
            return null;
        }

        declaration = performReplace(declaration, operation);
        if (declaration != null) {
            declaration = CodeInsightUtilCore.forcePsiPostprocessAndRestoreElement(declaration);
        }
        return declaration;
    }

    @Nullable
    public PsiElement createDeclaration(DartIntroduceOperation operation) {
        final Project project = operation.getProject();
        final DartExpression initializer = operation.getInitializer();
        InitializerTextBuilder builder = new InitializerTextBuilder();
        initializer.accept(builder);
        String assignmentText = getDeclarationString(operation, builder.result());
        return DartElementGenerator.createStatementFromText(project, assignmentText);
    }

    abstract protected String getDeclarationString(DartIntroduceOperation operation, String initExpression);

    @Nullable
    private PsiElement performReplace(@Nonnull final PsiElement declaration, final DartIntroduceOperation operation) {
        final DartExpression expression = operation.getInitializer();
        final Project project = operation.getProject();
        return new WriteCommandAction<PsiElement>(project, expression.getContainingFile()) {
            protected void run(final Result<PsiElement> result) throws Throwable {
                final PsiElement createdDeclaration = addDeclaration(operation, declaration);
                result.setResult(createdDeclaration);
                if (createdDeclaration != null) {
                    modifyDeclaration(createdDeclaration);
                }

                PsiElement newExpression = createExpression(project, operation.getName());

                if (operation.isReplaceAll()) {
                    List<PsiElement> newOccurrences = new ArrayList<PsiElement>();
                    for (PsiElement occurrence : operation.getOccurrences()) {
                        final PsiElement replaced = replaceExpression(occurrence, newExpression, operation);
                        if (replaced != null) {
                            newOccurrences.add(replaced);
                        }
                    }
                    operation.setOccurrences(newOccurrences);
                }
                else {
                    final PsiElement replaced = replaceExpression(expression, newExpression, operation);
                    operation.setOccurrences(Collections.singletonList(replaced));
                }

                postRefactoring(operation.getElement());
            }
        }.execute().getResultObject();
    }

    protected void modifyDeclaration(@Nonnull PsiElement declaration) {
        final PsiElement parent = declaration.getParent();

        PsiElement newLineNode = PsiParserFacade.SERVICE.getInstance(declaration.getProject()).createWhiteSpaceFromText("\n");
        parent.addAfter(newLineNode, declaration);

        final ASTNode nextChild = declaration.getNode().getTreeNext();
        parent.getNode().addLeaf(DartTokenTypes.SEMICOLON, ";", nextChild);
    }

    @Nullable
    protected DartReference createExpression(Project project, String name) {
        return DartElementGenerator.createReferenceFromText(project, name);
    }

    @Nullable
    protected PsiElement replaceExpression(PsiElement expression, PsiElement newExpression, DartIntroduceOperation operation) {
        return expression.replace(newExpression);
    }


    protected void postRefactoring(PsiElement element) {
    }

    @Nullable
    public PsiElement addDeclaration(DartIntroduceOperation operation, PsiElement declaration) {
        PsiElement anchor = operation.isReplaceAll() ? findAnchor(operation.getOccurrences()) : findAnchor(operation.getInitializer());
        if (anchor == null) {
            CommonRefactoringUtil.showErrorHint(operation.getProject(),
                operation.getEditor(),
                RefactoringBundle.getCannotRefactorMessage(DartLocalize.dartRefactoringIntroduceAnchorError().get()),
                DartLocalize.dartRefactoringIntroduceError().get(),
                null);
            return null;
        }
        final PsiElement parent = anchor.getParent();
        return parent.addBefore(declaration, anchor);
    }


    private static class DartInplaceVariableIntroducer extends InplaceVariableIntroducer<PsiElement> {
        private final DartComponentName myTarget;

        public DartInplaceVariableIntroducer(DartComponentName target, DartIntroduceOperation operation, List<PsiElement> occurrences) {
            super(target,
                operation.getEditor(),
                operation.getProject(),
                "Introduce Variable",
                occurrences.toArray(new PsiElement[occurrences.size()
                    ]),
                null);
            myTarget = target;
        }

        @Override
        protected PsiElement checkLocalScope() {
            return myTarget.getContainingFile();
        }
    }

    private static class InitializerTextBuilder extends PsiRecursiveElementVisitor {
        private final StringBuilder myResult = new StringBuilder();

        @Override
        public void visitWhiteSpace(PsiWhiteSpace space) {
            myResult.append(space.getText().replace('\n', ' '));
        }

        @Override
        public void visitElement(PsiElement element) {
            if (element.getChildren().length == 0) {
                myResult.append(element.getText());
            }
            else {
                super.visitElement(element);
            }
        }

        public String result() {
            return myResult.toString();
        }
    }
}
