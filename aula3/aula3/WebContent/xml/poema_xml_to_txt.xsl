<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="text" encoding="iso-8859-1" indent="yes"/>
	<xsl:template match="/poema">
		<xsl:text>Título: </xsl:text>
		<xsl:value-of select="título"/>
		<xsl:text>&#xa;</xsl:text> 
		<xsl:text>&#xa;</xsl:text>
		<xsl:for-each select="estrofe"> 
			<xsl:value-of select="position()"/>
			<xsl:text>ª Estrofe&#xa;</xsl:text>
			<xsl:for-each select="verso">
				<xsl:text>		</xsl:text>
				<xsl:value-of select="position()"/><xsl:text>º Verso </xsl:text><xsl:value-of select="."/>
				<xsl:text>&#xa;</xsl:text>
			</xsl:for-each>
		</xsl:for-each> 
		<xsl:text>&#xa;Autor: </xsl:text>
		<xsl:value-of select="autor"/>
	</xsl:template>
</xsl:stylesheet>
