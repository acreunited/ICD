<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="html" version="4.0" encoding="iso-8859-1" indent="yes"/>
	<xsl:template match="/poema">
		<html>
			<body>
				<xsl:apply-templates select="título"/><br/>
				<xsl:apply-templates select="estrofe"/>
				<xsl:apply-templates select="autor"/>
			</body>
		</html>
	</xsl:template>
	<xsl:template match="título">
  Título: <span style="color:#ff0000">
			<xsl:value-of select="."/>
		</span>
		<br/>
	</xsl:template>
	<xsl:template match="autor">
  Autor: <span style="color:#00ff00">
			<xsl:value-of select="."/>
		</span>
		<br/>
	</xsl:template>
	<xsl:template match="estrofe">
		<xsl:apply-templates select="verso"/>
		<br/>
	</xsl:template>
	<xsl:template match="verso">
		<span style="color:#003300">
			<xsl:value-of select="."/>
		</span>
		<br/>
	</xsl:template>
</xsl:stylesheet>
