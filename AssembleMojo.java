
package net.runelite.script;

import com.google.common.io.Files;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import net.runelite.cache.IndexType;
import net.runelite.cache.definitions.ScriptDefinition;
import net.runelite.cache.definitions.savers.ScriptSaver;
import net.runelite.cache.script.assembler.Assembler;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

@Mojo(
	name = "assemble",
	defaultPhase = LifecyclePhase.GENERATE_RESOURCES
)
public class AssembleMojo extends AbstractMojo
{
	@Parameter(required = true)
	private File scriptDirectory;

	@Parameter(required = true)
	private File outputDirectory;

	private final Log log = getLog();

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException
	{
		RuneLiteInstructions instructions = new RuneLiteInstructions();
		instructions.init();

		Assembler assembler = new Assembler(instructions);
		ScriptSaver saver = new ScriptSaver();

		int count = 0;
		File scriptOut = new File(outputDirectory, Integer.toString(IndexType.CLIENTSCRIPT.getNumber()));
		scriptOut.mkdirs();

		for (File scriptFile : scriptDirectory.listFiles((dir, name) -> name.endsWith(".rs2asm")))
		{
			log.debug("Assembling " + scriptFile);

			try (FileInputStream fin = new FileInputStream(scriptFile))
			{
				ScriptDefinition script = assembler.assemble(fin);
				byte[] packedScript = saver.save(script);

				File targetFile = new File(scriptOut, Integer.toString(script.getId()));
				Files.write(packedScript, targetFile);

				// Copy hash file

				File hashFile = new File(scriptDirectory, Files.getNameWithoutExtension(scriptFile.getName()) + ".hash");
				if (hashFile.exists())
				{
					Files.copy(hashFile, new File(scriptOut, Integer.toString(script.getId()) + ".hash"));
				}
				else if (script.getId() < 10000) // Scripts >=10000 are RuneLite scripts, so they shouldn't have a .hash
				{
					throw new MojoExecutionException("Unable to find hash file for " + scriptFile);
				}

				++count;
			}
			catch (IOException ex)
			{
				throw new MojoFailureException("unable to open file", ex);
			}
		}

		log.info("Assembled " + count + " scripts");
	}
}
