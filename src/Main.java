import java.io.IOException;
import java.util.List;

import com.change_vision.jude.api.inf.exception.LicenseNotFoundException;
import com.change_vision.jude.api.inf.exception.NonCompatibleException;
import com.change_vision.jude.api.inf.exception.ProjectLockedException;
import com.change_vision.jude.api.inf.exception.ProjectNotFoundException;
import com.change_vision.jude.api.inf.model.IClass;
import com.change_vision.jude.api.inf.model.INamedElement;
import com.change_vision.jude.api.inf.model.IState;
import com.change_vision.jude.api.inf.model.IStateMachine;
import com.change_vision.jude.api.inf.model.IStateMachineDiagram;
import com.change_vision.jude.api.inf.model.IVertex;

public class Main {

	public static void main(String[] args) throws ClassNotFoundException, LicenseNotFoundException,
			ProjectNotFoundException, NonCompatibleException, IOException, ProjectLockedException, Throwable {

		List stateContents;
		List vertex;
		INamedElement[] in;
		INamedElement ine;
		IStateMachine is;
		IVertex[] iVertexes;

		StateDiagramFinder sdf = new StateDiagramFinder("generate_model.asta");
		System.out.println("プロジェクト取得完了\r\n");
		

		/*
		 * ステートマシン図を取得
		 */
		stateContents = sdf.getIStateMachines(sdf.getiModel());
		System.out.println("状態遷移図要素数は" + stateContents.size() + "個です");
		for (int i = 0; i < stateContents.size(); i++) {
			System.out.println((i + 1) + "個目の状態遷移図の要素は" + stateContents.get(i));
		}
		System.out.println("project名は" + sdf.iModel + "です\r\n");

		/*
		 * 図から、ステートマシンを取得
		 */
		is = sdf.get((IStateMachineDiagram) stateContents.get(0));
		System.out.println("isは" + is + "です\r\n");
		/*
		 * 状態名を所得
		 */
		iVertexes = is.getVertexes();
		for (int i = 0; i < iVertexes.length; i++) {
			System.out.println((i + 1) + "個目の要素は" + iVertexes[i]);

		}
		System.out.println("\r\n");
		/*
		 * 入場動作を取得、表示
		 */ 
		for (int i = 0; i < iVertexes.length; i++) {
			if(iVertexes[i] instanceof IState)
			System.out.println(iVertexes[i]+"の入場動作は"+((IState)iVertexes[i]).getEntry());

		}
		System.out.println("\r\n");
		/*
		 * 実行活動を取得
		 */
		for (int i = 0; i < iVertexes.length; i++) {
			if(iVertexes[i] instanceof IState)
			System.out.println(iVertexes[i]+"の入場動作は"+((IState)iVertexes[i]).getDoActivity());

		}
	}
}