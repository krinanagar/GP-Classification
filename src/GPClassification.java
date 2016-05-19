import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jgap.InvalidConfigurationException;
import org.jgap.gp.CommandGene;
import org.jgap.gp.GPFitnessFunction;
import org.jgap.gp.GPProblem;
import org.jgap.gp.IGPProgram;
import org.jgap.gp.function.Add;
import org.jgap.gp.function.Add3;
import org.jgap.gp.function.Divide;
import org.jgap.gp.function.Multiply;
import org.jgap.gp.function.Subtract;
import org.jgap.gp.impl.DefaultGPFitnessEvaluator;
import org.jgap.gp.impl.GPConfiguration;
import org.jgap.gp.impl.GPGenotype;
import org.jgap.gp.terminal.Terminal;
import org.jgap.gp.terminal.Variable;
/**
 * 
 * @author Krina Nagar
 *
 */
public class GPClassification extends GPProblem {
	
	private static final String trainingFile = "training.txt";
    private static final String testFile = "test.txt";
    private static ParseData training;
    private static ParseData test;
   
    //create variables
    private static Variable clumpThickness;
    private static Variable uniformityOfCellSize;
    private static Variable uniformityOfCellShape;
    private static Variable marginalAdhesion;
    private static Variable singleEpithelialCellSize;
    private static Variable bareNuclei;
    private static Variable blandChromatin;
    private static Variable normalNucleoli;
    private static Variable mitoses;
    
    /**
     * Creates new genetic classfication algorithm
     * @param conf
     * @throws InvalidConfigurationException
     */
    public GPClassification(ParseData train, GPConfiguration conf) throws InvalidConfigurationException {
		super(conf);
	}
    
    /**
	 * @return GPGenotype
	 * @throws InvalidConfigurationException
	 */
	@Override
	public GPGenotype create() throws InvalidConfigurationException {
		GPConfiguration conf = getGPConfiguration();
		//return types
		Class[] types = {CommandGene.IntegerClass, CommandGene.IntegerClass}; 
		//argument types
		Class[][] argTypes = {{}, {CommandGene.IntegerClass, CommandGene.IntegerClass, CommandGene.IntegerClass}};
		//define function set
		CommandGene[][] nodeSets = {{
			clumpThickness = Variable.create(conf, "clumpThickness", CommandGene.IntegerClass),
                    uniformityOfCellSize = Variable.create(conf, "uniformityOfCellSize", CommandGene.IntegerClass),
                    uniformityOfCellShape = Variable.create(conf, "uniformityOfCellShape", CommandGene.IntegerClass),
                    marginalAdhesion = Variable.create(conf, "marginalAdhesion", CommandGene.IntegerClass),
                    singleEpithelialCellSize = Variable.create(conf, "singleEpithelialCellSize", CommandGene.IntegerClass),
                    bareNuclei = Variable.create(conf, "bareNuclei", CommandGene.IntegerClass),
                    blandChromatin = Variable.create(conf, "blandChromatin", CommandGene.IntegerClass),
                    normalNucleoli = Variable.create(conf, "normalNucleoli", CommandGene.IntegerClass),
                    mitoses = Variable.create(conf, "mitoses", CommandGene.IntegerClass),
			new Multiply(conf, CommandGene.IntegerClass),
			new Add(conf, CommandGene.IntegerClass),
			new Divide(conf, CommandGene.IntegerClass),
			new Subtract(conf, CommandGene.IntegerClass),
			new Terminal(conf, CommandGene.IntegerClass, -10.0d, 10.0d, true),
			
		},{
            new Add3(conf, CommandGene.IntegerClass),
    }};
		return GPGenotype.randomInitialGenotype(conf, types, argTypes, nodeSets,20, true);
	}
	
	/**
	 * Initialise genetic program configurations
	 * @return GPProblem
	 * @throws InvalidConfigurationException
	 */
	public static GPProblem initialiseConfigurations() throws InvalidConfigurationException{
		GPConfiguration config = new GPConfiguration();
		   
		 config.setGPFitnessEvaluator(new DefaultGPFitnessEvaluator());	
		 config.setMaxInitDepth(4);
		 config.setPopulationSize(1000);
		 config.setMaxCrossoverDepth(8);
		 config.setFitnessFunction(new GPClassification.FormulaFitnessFunction());
		 config.setStrictProgramCreation(true);
		 config.setCrossoverProb(0.8f);
	     config.setMutationProb(0.1f);
	     config.setReproductionProb(0.1f);
	     
	     GPProblem problem = new GPClassification(training, config);
		 return problem;
	}
	

	/**
	 * Fitness function for evaluating the produced formulas, represented as GP
	 * programs. The fitness is computed by calculating the result (Y) of the
	 * function/formula for integer inputs 0 to 20 (X). The sum of the differences
  	 * between expected Y and actual Y is the fitness, the lower the better (as
  	 * it is a defect rate here).
	 */
	 public static class FormulaFitnessFunction extends GPFitnessFunction {
		 
		 protected double evaluate(final IGPProgram a_subject) {
			 return computeRawFitness(a_subject); 
		 }
		 
		 /**
		  * Calculates raw fitness value for each generation provided
		  * @param prog
		  * @return fitness accuracy
		  */
		 public static double computeRawFitness(final IGPProgram prog) {
			 int correct = 0;
			 for(Instance i : training.getInstances()){
				 //set variables
				 clumpThickness.set(i.getClumpThickness());
	             uniformityOfCellSize.set(i.getUniformityOfCellSize());
	             uniformityOfCellShape.set(i.getUniformityOfCellShape());
	             marginalAdhesion.set(i.getMarginalAdhesion());
	             singleEpithelialCellSize.set(i.getSingleEpithelialCellSize());
	             bareNuclei.set(i.getBareNuclei());
	             blandChromatin.set(i.getBlandChromatin());
	             normalNucleoli.set(i.getNormalNucleoli());
	             mitoses.set(i.getMitoses());
	             
	             //calculate fitness
				 int result = prog.execute_int(0, new Object[0]);
				 int classification = 0;
				 if(result < 0){
					 classification = 4;
				 }
				 else{
					 classification = 2;
				 }
				 //check if it matches
				 if(classification == i.getCancerClass()){
					 correct++;
				 }
			 }
			//calculate accuracy for training
			double correctPercent = ((double)correct/(double)training.getInstances().size())*100;
			return correctPercent;
			 
		 }
	 }
	 
	 public static void main(String[] args) throws Exception {
		 
		 training = new ParseData(trainingFile);
		 
		 GPProblem problem = initialiseConfigurations();
		 GPGenotype gp = problem.create();
		 gp.setVerboseOutput(true);
		 for (int gen = 0; gen <= 1000; gen++) {
			gp.evolve();
			gp.calcFitness();
			if(gp.getAllTimeBest().getFitnessValue() >= 99){
				System.out.println(gen);
				break;
			}
		 }
		 System.out.println("BEST SOLUTION:");
		 gp.outputSolution(gp.getAllTimeBest());
		 
		 //check accuracy
		 double accuracyTrain = checkForumla(gp.getAllTimeBest(), trainingFile);
		 double accuracyTest = checkForumla(gp.getAllTimeBest(), testFile);
		 System.out.println("CORRECT CLASSIFICATIONS(Training):" + accuracyTrain);
		 System.out.println("CORRECT CLASSIFICATIONS(Test):" + accuracyTest);
	 }
	 
	 /**
	  * Takes a file and calculates its fitness based on the all time best
	  * forumla produced when trained.
	  * @param allTimeBest
	  * @param file
	  * @return classification accuracy
	 * @throws IOException 
	  */
	private static double checkForumla(IGPProgram allTimeBest, String file) throws IOException {
		ParseData d = new ParseData(file);
		int hits = 0;
		 for(Instance i : d.getInstances()){
			 //set variables
			 clumpThickness.set(i.getClumpThickness());
             uniformityOfCellSize.set(i.getUniformityOfCellSize());
             uniformityOfCellShape.set(i.getUniformityOfCellShape());
             marginalAdhesion.set(i.getMarginalAdhesion());
             singleEpithelialCellSize.set(i.getSingleEpithelialCellSize());
             bareNuclei.set(i.getBareNuclei());
             blandChromatin.set(i.getBlandChromatin());
             normalNucleoli.set(i.getNormalNucleoli());
             mitoses.set(i.getMitoses());
             
             //calculate fitness
			 int result = allTimeBest.execute_int(0, new Object[0]);
			 int classification = 0;
			 
			 if(result < 0){
				 classification = 4;
			 }
			 else{
				 classification = 2;
			 }
			 //check if it matches
			 if(classification == i.getCancerClass()){
				 hits++;
			 }
		 }
		//calculate accuracy
		double correctPercent = ((double)hits/(double)d.getInstances().size())*100;
		return correctPercent;
	}
}
