package de.contentup.montreee.cli

import amber.source.provider.FileContentSource
import de.contentup.montreee.versionFromString

val version by lazy { versionFromString(FileContentSource("build.txt").read()) }