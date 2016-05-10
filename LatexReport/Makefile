FILENAME=thesis
TEXBIN=/Library/TeX/texbin
${FILENAME}.pdf: ${FILENAME}.tex chap1.tex chap2.tex chap3.tex chap4.tex bib.tex
	${TEXBIN}/pdflatex ${FILENAME}.tex
	#${TEXBIN}/bibtex ${FILENAME}
	${TEXBIN}/pdflatex ${FILENAME}.tex
	${TEXBIN}/pdflatex ${FILENAME}.tex
	#${TEXBIN}/dvips ${FILENAME}.dvi
	#ps2pdf ${FILENAME}.ps # on path
	open ${FILENAME}.pdf

clean:
	-rm *.dvi
	-rm *.bbl
	-rm *.blg
	-rm *.ps
	-rm *.log
	-rm *.aux
	-rm *.ps.plog
	-rm *.ps

spotless: clean
	-rm ${FILENAME}.pdf

