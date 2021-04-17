package Model;

import java.io.FileWriter;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

public class Commands {
	
	// Default IO interface
	public interface DefaultIO{
		public String readText();
		public void write(String text);
		public float readVal();
		public void write(float val);

		// you may add default methods here
	}



	// the default IO to be used in all commands
	DefaultIO dio;
	public Commands(DefaultIO dio) {
		this.dio=dio;
	}
	
	// you may add other helper classes here
	
	
	
	// the shared state of all commands
	private class SharedState{

		TimeSeries train = null;

		TimeSeries test = null;

		List<AnomalyReport>  anomalyList = null;  //????

		List<Point> pointList = null;

	}

	private SharedState sharedState=new SharedState();
	public SharedState getSharedState()
	{
		return sharedState;
	}


	public DefaultIO getDio() {
		return dio;
	}


	// Command abstract class
	public abstract class Command{
		protected String description;
		
		public Command(String description) {
			this.description=description;
		}
		
		public abstract void execute();
	}
	
	// Command class for example:
	public class ExampleCommand extends Command{

		public ExampleCommand() {
			super("this is an example of command");
		}

		@Override
		public void execute() {
			dio.write(description);
		}		
	}


	public class menuCommand extends Command{

		public menuCommand() {

			super("Welcome to the Anomaly Detection Server.\n"+
					"Please choose an option:\n"+
					"1. upload a time series csv file\n"+
					"2. algorithm settings\n"+
					"3. detect anomalies\n"+
					"4. display results\n"+
					"5. upload anomalies and analyze results\n"+
					"6. exit\n");
		}

		@Override
		public void execute() {

			dio.write(description);

			String readSelection = dio.readText();

			int selection= Integer.parseInt(readSelection);

			switch (selection) {
				case 1: // upload csv
					new uploadCSVFileCommand().execute();;
					break;

				case 2:  // algo settings
					new algoSettingsCommand().execute();
					break;

				case 3:  // detect
					new detectAnomaliesCommand().execute();
					break;

				case 4:  // display
					new displayResultsCommand().execute();
					break;

				case 5:  // upload anomalies and analyze
					new uploadAndAnalyzeCommand().execute();
					break;

				case 6:  // exit
					new exitCommand().execute();
					break;

			}

		}
	}  // end of menu command


	public class uploadCSVFileCommand extends Command{

		public uploadCSVFileCommand() {
			super("Please upload your local train CSV file.\n");
		}


		@Override
		public void execute() {

			///// file 1 /////

			dio.write(description);

			String line = null;

			ArrayList<String> read = new ArrayList<>();

			while (!(line=dio.readText()).equals("done"))
			{
				read.add(line);

				read.add("\n");
			}

			try {

				FileWriter csvFile= new FileWriter("anomalyTrain.csv");

				String all = read.stream().collect(Collectors.joining(""));

				csvFile.write(all);

				csvFile.close();

			}

			catch (Exception e) {
				e.printStackTrace();
			}

			getSharedState().train=new TimeSeries("anomalyTrain.csv");

			dio.write("Upload complete.\n");



					///// file 2 /////

			dio.write("Please upload your local test CSV file.\n");

			String line2 = null;

			ArrayList<String> read2 = new ArrayList<>();

			while (!(line2=dio.readText()).equals("done"))
			{
				read2.add(line2);

				read2.add("\n");
			}

			try {

				FileWriter csvFile= new FileWriter("anomalyTest.csv");

				String all2 = read2.stream().collect(Collectors.joining(""));

				csvFile.write(all2);

				csvFile.close();

			}

			catch (Exception e) {
				e.printStackTrace();
			}

			getSharedState().test=new TimeSeries("anomalyTest.csv");

			dio.write("Upload complete.\n");

			new menuCommand().execute();

		}

	}


	public class algoSettingsCommand extends Command{

		String msg;

		double treshold;

		public algoSettingsCommand() {

			super("");

			treshold=getSharedState().train.correlationTresh;

			msg = "The current correlation treshold is ";
			msg = msg.concat(String.valueOf(treshold));
			msg = msg.concat("\nType a new threshold\n");

		}

		@Override
		public void execute()
		{
			dio.write(msg);

			double tempTresh = Double.parseDouble(dio.readText());

			while (tempTresh>1||tempTresh<0)
			{
				dio.write("please choose a value between 0 and 1.");

				tempTresh = Double.parseDouble(dio.readText());

			}

			getSharedState().train.setCorrelationTresh(tempTresh);

			new menuCommand().execute();

		}
	}

	public class detectAnomaliesCommand extends Command{

		public detectAnomaliesCommand()
		{
			super("anomaly detection complete.\n");
		}

		@Override
		public void execute()
		{

			LinearRegression SAD = new LinearRegression();

			SAD.learnNormal(getSharedState().train);

			List<AnomalyReport> tempList = new ArrayList<>();

			tempList.addAll(SAD.detect(getSharedState().test));

			getSharedState().anomalyList = tempList;

			dio.write(description);

			new menuCommand().execute();

		}
	}





	public class displayResultsCommand extends Command{

		public displayResultsCommand()
		{

			super("Done.\n");

		}


		@Override
		public void execute()
		{
			for (AnomalyReport ar: getSharedState().anomalyList)
				dio.write((String.valueOf(ar.timeStep))+ "\t" + ar.description + "\n");

			dio.write(description);

			new menuCommand().execute();
		}
	}














	public class uploadAndAnalyzeCommand extends Command{

		public uploadAndAnalyzeCommand()
		{
			super("Please upload your local anomalies file.\n" + "Upload complete.\n" );
		}

		@Override
		public void execute()
		{

			dio.write(description);

			float rangeSum=0;

			String line=null;

			getSharedState().pointList=new ArrayList<>();

			while (!((line = dio.readText()).equals("done")))
			{

				String [] temp = line.split(",");

				getSharedState().pointList.add(new Point(Float.valueOf(temp[0]),Float.valueOf(temp[1]) ));

			}

			for (Point p:getSharedState().pointList)
			  	rangeSum+= p.y - p.x + 1;

			float N = getSharedState().test.getCols()[0].getFloats().size()- rangeSum;     ///////////////////////////



			ArrayList<String> discription = new ArrayList<>();

			ArrayList<Point> time = new ArrayList<>();

			int q=0;

			if (getSharedState().anomalyList==null)
				return;

			for (int i=0; i<getSharedState().anomalyList.size();i++)
			{
				AnomalyReport start = getSharedState().anomalyList.get(i);

				AnomalyReport end = null;

				int j = i+1;

				while (j<getSharedState().anomalyList.size()
						&& start.description.equals(getSharedState().anomalyList.get(j).description)
						&& start.timeStep + j - q == getSharedState().anomalyList.get(j).timeStep)
				{
					end = getSharedState().anomalyList.get(j);

					j++;

				}

				if (end != null)
				{
					discription.add(end.description);

					time.add(new Point(start.timeStep, end.timeStep));


				}

				else
				{
					discription.add(start.description);

					time.add(new Point(start.timeStep, start.timeStep));

				}

				q=j;

				i = j-1;

			}

			float FP = 0;

			float TP = 0;

			float TN = 0;

			float FN = 0;

			float P = getSharedState().pointList.size();

			boolean flag = false;

			List<Point> pointList= new ArrayList<>();

			for (Point p: getSharedState().pointList)
			{
				for (Point k:time)
				{
					if ((p.x <= k.x && k.y <= p.y)
							|| (p.x >= k.x && p.y >= k.y && k.y >= p.x)
							|| (k.x <= p.x && p.y <= k.y)
							|| (p.x <= k.x && p.y <= k.y && k.x <= p.y))
					{
						if (!flag)
							TP++;

						flag = true;

						if (!pointList.contains(k))
							pointList.add(k);
					}

				}

				if (!flag)
					FN++;

				flag = false;
			}

			FP = (time.size() - pointList.size()) / N;

			TP /= P;

			DecimalFormat decimalFormat = new DecimalFormat("0.0000");

			String tp = decimalFormat.format(TP);

			String fp = decimalFormat.format(FP);

			tp = tp.substring(0,tp.length()-1);

			fp = fp.substring(0,fp.length()-1);

			while (tp.charAt(tp.length()-1)=='0' && tp.length()!=3)
				tp = tp.substring(0,tp.length()-1);

			while (fp.charAt(fp.length()-1)=='0' && fp.length()!=3)
				fp = fp.substring(0,fp.length()-1);

			dio.write("True Positive Rate: " + tp + "\n");

			dio.write("False Positive Rate: " + fp + "\n");

			new menuCommand().execute();

		}
	}


	public class exitCommand extends Command{

		public exitCommand() {
			super("");
		}

		@Override
		public void execute() {
			dio.write(description);
		}
	}

}
	

