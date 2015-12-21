import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.change_vision.jude.api.inf.AstahAPI;
import com.change_vision.jude.api.inf.exception.LicenseNotFoundException;
import com.change_vision.jude.api.inf.exception.NonCompatibleException;
import com.change_vision.jude.api.inf.exception.ProjectLockedException;
import com.change_vision.jude.api.inf.exception.ProjectNotFoundException;
import com.change_vision.jude.api.inf.model.IAttribute;
import com.change_vision.jude.api.inf.model.IClass;
import com.change_vision.jude.api.inf.model.IConstraint;
import com.change_vision.jude.api.inf.model.IDiagram;
import com.change_vision.jude.api.inf.model.IElement;
import com.change_vision.jude.api.inf.model.IGeneralization;
import com.change_vision.jude.api.inf.model.IModel;
import com.change_vision.jude.api.inf.model.INamedElement;
import com.change_vision.jude.api.inf.model.IOperation;
import com.change_vision.jude.api.inf.model.IPackage;
import com.change_vision.jude.api.inf.model.IParameter;
import com.change_vision.jude.api.inf.model.IState;
import com.change_vision.jude.api.inf.model.IStateMachine;
import com.change_vision.jude.api.inf.model.IStateMachineDiagram;
import com.change_vision.jude.api.inf.model.IVertex;
import com.change_vision.jude.api.inf.project.ProjectAccessor;

public class StateDiagramFinder {

	private static final String EMPTY_COLUMN = "";
	ProjectAccessor prjAccessor;
	String inputFile;
	IModel iModel;
	INamedElement[] iNamedElements;

	/**
	 * @param inputFile
	 *            File to input
	 */
	public StateDiagramFinder(String inputFile) throws LicenseNotFoundException, ProjectNotFoundException,
			NonCompatibleException, IOException, ClassNotFoundException, ProjectLockedException, Throwable {
		this.inputFile = inputFile;
		// Open a project. And get the model.
		ProjectAccessor prjAccessor = AstahAPI.getAstahAPI().getProjectAccessor();
		prjAccessor.open(inputFile);
		iModel = prjAccessor.getProject();
		prjAccessor.close();
	}

	/**
	 * Get states in selected package.
	 * 
	 * @param iPackage
	 *            Selected package
	 * @return List of all stored classes
	 */
	// public List getIStateMachines(IPackage iPackage) {
	// List iStateMachines = new ArrayList();
	// iNamedElements = iPackage.getOwnedElements();
	// for (int i = 0; i < iNamedElements.length; i++) {
	// INamedElement iNamedElement = iNamedElements[i];
	// if (iNamedElement instanceof IStateMachineDiagram) {
	// iStateMachines.add(iNamedElement);
	// }
	// }
	// return iStateMachines;
	// }

	/*
	 * テストステートマシン図取得
	 */

	public  IStateMachine get(IStateMachineDiagram is) {
		IStateMachine iStates;
		iStates= is.getStateMachine();
		return iStates;
	}

	/**
	 * Get states in selected package.
	 * 
	 * @param iPackage
	 *            Selected package
	 * @return List of all stored classes
	 */
	public List getIStateMachines(INamedElement in) {
		List iStateMachines = new ArrayList();
		iNamedElements = ((IPackage) in).getDiagrams();
		for (int i = 0; i < iNamedElements.length; i++) {
			INamedElement iNamedElement = iNamedElements[i];
			if (iNamedElement instanceof IStateMachineDiagram) {
				iStateMachines.add(iNamedElement);
			}
		}
		return iStateMachines;
	}

	/*
	 * 状態を取得、それをリストで返却
	 */

	public List getVertexes(INamedElement in) {
		List vertexes = new ArrayList();

		if (in instanceof IPackage) {
			IVertex[] iv = ((IStateMachine) in).getVertexes();
			for (int i = 0; i < iv.length; i++) {
				vertexes.add(iv[i]);
			}
		}

		return vertexes;
	}

	/**
	 * 
	 * @param iNamedElement
	 * @return
	 */
	public List getAllStateMachineDiagrams(INamedElement iNamedElement) {
		List diagrams = new ArrayList();
		IDiagram[] dgms = iNamedElement.getDiagrams();
		for (int i = 0; i < dgms.length; i++) {
			if (dgms[i] instanceof IStateMachineDiagram) {
				diagrams.add(dgms[i]);
			}
		}
		return diagrams;
	}

	/**
	 * Get generalization class names.
	 * 
	 * @param iClass
	 *            Class
	 * @return Class name
	 */
	public String getSuperClass(IClass iClass) {
		StringBuilder buffer = new StringBuilder();
		IGeneralization[] generalizations = iClass.getGeneralizations();
		for (int i = 0; i < generalizations.length; i++) {
			IClass superClass = generalizations[i].getSuperType();
			buffer.append(getFullName(superClass));
			if (i != generalizations.length - 1) {
				buffer.append(", ");
			}
		}
		return buffer.toString();
	}

	/**
	 * Get Class name as Full Path.
	 * 
	 * @param iClass
	 *            Class
	 * @return Class Name (Full Path)
	 */
	public String getFullName(IClass iClass) {
		StringBuilder sb = new StringBuilder();
		IElement owner = iClass.getOwner();
		while (owner != null && owner instanceof INamedElement && owner.getOwner() != null) {
			sb.insert(0, ((INamedElement) owner).getName() + "::");
			owner = owner.getOwner();
		}
		sb.append(iClass.getName());
		return sb.toString();
	}

	/**
	 * Get all Attribute information.
	 * 
	 * @param iClass
	 *            Class
	 * @return All information of all attributes (String List stored in the
	 *         list)
	 */
	public List getAttributeLines(IClass iClass) {
		List lines = new ArrayList();
		IAttribute[] iAttributes = iClass.getAttributes();
		for (int i = 0; i < iAttributes.length; i++) {
			IAttribute iAttribute = iAttributes[i];
			lines.add(getAttributeLine(iAttribute));
		}
		return lines;
	}

	/**
	 * Get Attribute information.
	 * 
	 * @param iAttribute
	 *            Attribute
	 * @return Attribute information (String List)
	 */
	public List getAttributeLine(IAttribute iAttribute) {
		List line = new ArrayList();
		// line.add(EMPTY_COLUMN);
		line.add(getAttributeSignature(iAttribute));
		// line.add(iAttribute.getDefinition());
		// line.add(EMPTY_COLUMN);
		// line.add(EMPTY_COLUMN);
		return line;
	}

	/**
	 * Get attribute signature.
	 * 
	 * @param iAttribute
	 *            Attribute
	 * @return Attribute signature
	 */
	private String getAttributeSignature(IAttribute iAttribute) {
		String visibility = getVisibility(iAttribute);

		String name = iAttribute.getName();

		String type = iAttribute.getTypeExpression();

		String initValue = iAttribute.getInitialValue();
		if (initValue.length() > 0) {
			initValue = " = " + initValue;
		}

		IConstraint[] constraints = iAttribute.getConstraints();
		String constraint = "";
		for (int j = 0; j < constraints.length; j++) {
			constraint = constraint + "{" + constraints[j].getName() + "}";
		}

		// return visibility + " " + name + " : " + type + initValue +
		// constraint;
		return name + " : " + type + initValue + constraint;

	}

	/**
	 * Get all operation information.
	 * 
	 * @param iClass
	 *            Class
	 * @return All operation information (String list stored in the list)
	 */
	public List getOperationLines(IClass iClass) {
		List lines = new ArrayList();
		IOperation[] iOperations = iClass.getOperations();
		for (int i = 0; i < iOperations.length; i++) {
			IOperation iOperation = iOperations[i];
			lines.add(getOperationLine(iOperation));
		}
		return lines;
	}

	/**
	 * Get operation information.
	 * 
	 * @param iOperation
	 *            Operation
	 * @return Operation information (String List)
	 */
	public List getOperationLine(IOperation iOperation) {
		List line = new ArrayList();
		line.add(EMPTY_COLUMN);
		line.add(getOperationSignature(iOperation));
		line.add(iOperation.getDefinition());
		line.add(EMPTY_COLUMN);
		line.add(EMPTY_COLUMN);
		return line;
	}

	/**
	 * Get operation signature.
	 * 
	 * @param iOperation
	 *            Operation
	 * @return Operation signature
	 */
	public String getOperationSignature(IOperation iOperation) {
		String param = "";

		IParameter[] parameters = iOperation.getParameters();
		for (int i = 0; i < parameters.length; i++) {
			String paramName = parameters[i].getName();
			String paramType = parameters[i].getTypeExpression();
			param = param + paramName + " : " + paramType;
			if (i != parameters.length - 1) {
				param += ", ";
			}
		}
		param = "(" + param + ")";

		IConstraint[] constraints = iOperation.getConstraints();
		String constraint = "";
		for (int i = 0; i < constraints.length; i++) {
			constraint = constraint + "{" + constraints[i].getName() + "}";
		}

		String visibility = getVisibility(iOperation);
		String name = iOperation.getName();
		String returnType = iOperation.getReturnTypeExpression();
		if (returnType.length() > 0) {
			return visibility + " " + name + param + " : " + returnType;
		} else {
			return visibility + " " + name + param;
		}
	}

	/**
	 * Get visibility as string.
	 * 
	 * @param iNamedElement
	 *            Named elements
	 * @return Visibility
	 */
	public String getVisibility(INamedElement iNamedElement) {
		if (iNamedElement.isPackageVisibility()) {
			return "package";
		} else if (iNamedElement.isProtectedVisibility()) {
			return "protected";
		} else if (iNamedElement.isPrivateVisibility()) {
			return "private";
		} else if (iNamedElement.isPublicVisibility()) {
			return "public";
		}
		return "";
	}

	public IModel getiModel() {
		return iModel;
	}
}
