<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="text" />

	<xsl:template match="dependencies">
		<xsl:apply-templates select="dependency[@type='zip']">
			<xsl:sort select="position()" order="descending" data-type="number" />
		</xsl:apply-templates>
	</xsl:template>

	<xsl:template match="dependency">
		<xsl:if test="position()!=1">
			<xsl:text>,</xsl:text>
		</xsl:if>
		<xsl:value-of select="concat('components/', @groupId, '-', @artifactId)" />
	</xsl:template>

</xsl:stylesheet>
