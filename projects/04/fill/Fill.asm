	@screen
	M=0
(LOOP)
	@24576
	D=M
	@DRAW
	D;JNE
	@CLEAN
	D;JEQ
(DRAW)
	@screen
	D=M
	@SCREEN
	A=A+D
	M=-1
	@8192
	D=D-A
	@PLUS
	D;JLT
	@LOOP
	0;JMP
(PLUS)
	@screen
	M=M+1
	@LOOP
	0;JMP
(CLEAN)
	@screen
	D=M
	@SCREEN
	A=A+D
	M=0
	@MINUS
	D;JGT
	@LOOP
	0;JMP
(MINUS)
	@screen
	M=M-1
	@LOOP
	0;JMP