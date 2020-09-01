runelite.script;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import static java.lang.Integer.parseInt;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

@Mojo(
	name = "build-index",
	defaultPhase = LifecyclePhase.GENERATE_RESOURCES
)
public class IndexMojo extends AbstractMojo
{
	@Parameter(required = true)
	private File archiveOverlayDirectory;

	@Parameter(required = true)
	private File indexFile;

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException
	{
		try (DataOutputStream fout = new DataOutputStream(new FileOutputStream(indexFile)))
		{
			for (File indexFolder : archiveOverlayDirectory.listFiles())
			{
				if (indexFolder.isDirectory())
				{
					int indexId = parseInt(indexFolder.getName());
					for (File archiveFile : indexFolder.listFiles())
					{
						int archiveId;
						try
						{
							archiveId = parseInt(archiveFile.getName());
						}
						catch (NumberFormatException ex)
						{
							continue;
						}

						fout.writeInt(indexId << 16 | archiveId);
					}
				}
			}

			fout.writeInt(-1);
		}
		catch (IOException ex)
		{
			throw new MojoExecutionException("error build index file", ex);
		}
	}

}
