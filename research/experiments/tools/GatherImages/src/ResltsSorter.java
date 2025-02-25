import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class ResltsSorter
{
	public static void main(String[] args) throws IOException
	{
		String root = "E:\\gui\\_hp\\mt\\apps\\";

		for (String folderName : new File(root).list())
		{
			File folder = new File(root + "/" + folderName);

			if (folder.isDirectory())
			{
				moveResults(folder, "nokia31", "e:/_r/");
			}
		}		
	}
	
	private static void moveResults(File appFolder, String deviceName, String resultsFolder) throws IOException
	{
		File destination = new File(resultsFolder + deviceName + "/" + appFolder.getName());

		if (false == destination.exists())
		{
			File source = new File(appFolder.getAbsolutePath() + "/_results_/" + deviceName);
			
			if (source.exists())
			{
				Files.move(source.toPath(), destination.toPath());
				
				File appResultsFolder = new File(appFolder.getAbsolutePath() + "/_results_/");
				
				if (appResultsFolder.list().length == 0)
				{
					appResultsFolder.delete();
				}
			}
			else
			{
				System.out.println("No source " + source.getAbsolutePath());
			}
		}
		else
		{
			System.out.println(destination.getAbsolutePath() + " already exists");
		}
	}
}
