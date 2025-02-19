package com.intellij.ide.starter.models

import com.intellij.ide.starter.ide.command.MarshallableCommand
import com.intellij.ide.starter.project.ProjectInfo
import com.intellij.ide.starter.project.ProjectInfoSpec

data class StartUpPerformanceCase(
  val ideInfo: IdeInfo,
  val projectInfo: ProjectInfoSpec? = null,
  val commands: Iterable<MarshallableCommand> = listOf(),
  val vmOptionsFix: VMOptions.() -> VMOptions = { this },
  val useInMemoryFileSystem: Boolean = false
) {
  fun withProject(projectInfo: ProjectInfoSpec): StartUpPerformanceCase = copy(projectInfo = projectInfo)

  fun withCommands(commands: Iterable<MarshallableCommand> = this.commands): StartUpPerformanceCase = copy(commands = commands.toList())

  fun markNotReusable(): StartUpPerformanceCase = markReusable(false)

  fun markReusable(isReusable: Boolean = true) = copy(projectInfo = (projectInfo as ProjectInfo).copy(isReusable = isReusable))

  fun makeEap(product: String = "Idea"): StartUpPerformanceCase {
    return copy(
      ideInfo = IdeInfo.new(
        this.ideInfo.productCode,
        this.ideInfo.platformPrefix,
        this.ideInfo.executableFileName,
        "ijplatform_IJPlatform213_${product}_InstallersForEapRelease",
        tag = "EAP",
      )
    )
  }

  fun makeSpecificBuild(buildNumber: String): StartUpPerformanceCase {
    return copy(
      ideInfo = IdeInfo.new(
        productCode = this.ideInfo.productCode,
        platformPrefix = this.ideInfo.platformPrefix,
        executableFileName = this.ideInfo.executableFileName,
        jetBrainsCIBuildType = this.ideInfo.buildType,
        buildNumber = buildNumber
      )
    )
  }

  @Suppress("unused")
  fun makeRelease(branch: String, product: String = "Idea"): StartUpPerformanceCase {
    return copy(
      ideInfo = IdeInfo.new(
        this.ideInfo.productCode,
        this.ideInfo.platformPrefix,
        this.ideInfo.executableFileName,
        "ijplatform_IJPlatform${branch}_${product}_InstallersForEapRelease",
        tag = "RELEASE",
      )
    )
  }
}