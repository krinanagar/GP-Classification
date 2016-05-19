import java.util.HashMap;
/**
 * Class represents a patient in the breast-cancer data file containing the
 * 10 attributes plus the patients id number. 
 * @author Krina Nagar
 *
 */
public class Instance {

	private int ID;
	private int clumpThickness;
	private int uniformityOfCellSize;
	private int uniformityOfCellShape;
	private int marginalAdhesion;
	private int singleEpithelialCellSize;
	private int bareNuclei;
	private int blandChromatin;
	private int normalNucleoli;
	private int mitoses;
	private int cancerClass;
	
	public Instance(HashMap<String, Integer> patientDetails) {
		//intialise variables to put matching attributes from map
        this.ID = patientDetails.get("ID");
        this.clumpThickness = patientDetails.get("clumpThickness");
        this.uniformityOfCellSize = patientDetails.get("uniformityOfCellSize");
        this.uniformityOfCellShape = patientDetails.get("uniformityOfCellShape");
        this.marginalAdhesion = patientDetails.get("marginalAdhesion");
        this.singleEpithelialCellSize = patientDetails.get("singleEpithelialCellSize");
        this.bareNuclei = patientDetails.get("bareNuclei");
        this.blandChromatin = patientDetails.get("blandChromatin");
        this.normalNucleoli = patientDetails.get("normalNucleoli");
        this.mitoses = patientDetails.get("mitoses");
        this.cancerClass = patientDetails.get("cancerClass");
    }

	public int getID() {
		return ID;
	}

	public int getClumpThickness() {
		return clumpThickness;
	}

	public int getUniformityOfCellSize() {
		return uniformityOfCellSize;
	}

	public int getUniformityOfCellShape() {
		return uniformityOfCellShape;
	}

	public int getMarginalAdhesion() {
		return marginalAdhesion;
	}

	public int getSingleEpithelialCellSize() {
		return singleEpithelialCellSize;
	}

	public int getBareNuclei() {
		return bareNuclei;
	}

	public int getBlandChromatin() {
		return blandChromatin;
	}

	public int getNormalNucleoli() {
		return normalNucleoli;
	}

	public int getMitoses() {
		return mitoses;
	}

	public int getCancerClass() {
		return cancerClass;
	}
	
}
