<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="xml" version="1.0" encoding="iso-8859-1" indent="yes"/>
	<xsl:template match="/poema">
		<poema>
			<xsl:apply-templates select="autor"/>
			<xsl:apply-templates select="t�tulo"/>
			<xsl:apply-templates select="estrofe"/>
		</poema>
	</xsl:template>
	<xsl:template match="autor">
		<autor>
			<xsl:value-of select="."/>
		</autor>
	</xsl:template>
	<xsl:template match="t�tulo">
		<t�tulo>
			<xsl:value-of select="."/>
		</t�tulo>
	</xsl:template>
	<xsl:template match="verso">
		<verso>
			<xsl:value-of select="."/>
		</verso>
	</xsl:template>
	<xsl:template match="estrofe">
		<xsl:variable name="nversos" select="count(verso)"/>
		<xsl:if test="$nversos=1">
			<mon�stico>
				<xsl:apply-templates select="verso"/>
			</mon�stico>
		</xsl:if>
		<xsl:if test="$nversos=2">
			<parelha>
				<xsl:apply-templates select="verso"/>
			</parelha>
		</xsl:if>
		<xsl:if test="$nversos=3">
			<terceto>
				<xsl:apply-templates select="verso"/>
			</terceto>
		</xsl:if>
		<xsl:if test="$nversos=4">
			<quadra>
				<xsl:apply-templates select="verso"/>
			</quadra>
		</xsl:if>
		<xsl:if test="$nversos=5">
			<quintilha>
				<xsl:apply-templates select="verso"/>
			</quintilha>
		</xsl:if>
		<xsl:if test="$nversos=6">
			<sextilha>
				<xsl:apply-templates select="verso"/>
			</sextilha>
		</xsl:if>
		<xsl:if test="$nversos=7">
			<s�tima>
				<xsl:apply-templates select="verso"/>
			</s�tima>
		</xsl:if>
		<xsl:if test="$nversos=8">
			<oitava>
				<xsl:apply-templates select="verso"/>
			</oitava>
		</xsl:if>
			<xsl:if test="$nversos=9">
			<nona>
				<xsl:apply-templates select="verso"/>
			</nona>
		</xsl:if>
		<xsl:if test="$nversos=10">
			<d�cima>
				<xsl:apply-templates select="verso"/>
			</d�cima>
		</xsl:if>
	</xsl:template>
</xsl:stylesheet>
