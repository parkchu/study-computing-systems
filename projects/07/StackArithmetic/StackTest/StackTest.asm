@17
D=A
@SP
A=M
M=D
@SP
M=M+1
@17
D=A
@SP
A=M
M=D
@SP
M=M+1
@28
D=A
@address
M=D
@SP
AM=M-1
D=M
M=0
A=A-1
D=M-D
@TRUE
D;JEQ
@FALSE
D;JNE
@17
D=A
@SP
A=M
M=D
@SP
M=M+1
@16
D=A
@SP
A=M
M=D
@SP
M=M+1
@56
D=A
@address
M=D
@SP
AM=M-1
D=M
M=0
A=A-1
D=M-D
@TRUE
D;JEQ
@FALSE
D;JNE
@16
D=A
@SP
A=M
M=D
@SP
M=M+1
@17
D=A
@SP
A=M
M=D
@SP
M=M+1
@84
D=A
@address
M=D
@SP
AM=M-1
D=M
M=0
A=A-1
D=M-D
@TRUE
D;JEQ
@FALSE
D;JNE
@892
D=A
@SP
A=M
M=D
@SP
M=M+1
@891
D=A
@SP
A=M
M=D
@SP
M=M+1
@112
D=A
@address
M=D
@SP
AM=M-1
D=M
M=0
A=A-1
D=M-D
@TRUE
D;JLT
@FALSE
D;JGE
@891
D=A
@SP
A=M
M=D
@SP
M=M+1
@892
D=A
@SP
A=M
M=D
@SP
M=M+1
@140
D=A
@address
M=D
@SP
AM=M-1
D=M
M=0
A=A-1
D=M-D
@TRUE
D;JLT
@FALSE
D;JGE
@891
D=A
@SP
A=M
M=D
@SP
M=M+1
@891
D=A
@SP
A=M
M=D
@SP
M=M+1
@168
D=A
@address
M=D
@SP
AM=M-1
D=M
M=0
A=A-1
D=M-D
@TRUE
D;JLT
@FALSE
D;JGE
@32767
D=A
@SP
A=M
M=D
@SP
M=M+1
@32766
D=A
@SP
A=M
M=D
@SP
M=M+1
@196
D=A
@address
M=D
@SP
AM=M-1
D=M
M=0
A=A-1
D=M-D
@TRUE
D;JGT
@FALSE
D;JLE
@32766
D=A
@SP
A=M
M=D
@SP
M=M+1
@32767
D=A
@SP
A=M
M=D
@SP
M=M+1
@224
D=A
@address
M=D
@SP
AM=M-1
D=M
M=0
A=A-1
D=M-D
@TRUE
D;JGT
@FALSE
D;JLE
@32766
D=A
@SP
A=M
M=D
@SP
M=M+1
@32766
D=A
@SP
A=M
M=D
@SP
M=M+1
@252
D=A
@address
M=D
@SP
AM=M-1
D=M
M=0
A=A-1
D=M-D
@TRUE
D;JGT
@FALSE
D;JLE
@57
D=A
@SP
A=M
M=D
@SP
M=M+1
@31
D=A
@SP
A=M
M=D
@SP
M=M+1
@53
D=A
@SP
A=M
M=D
@SP
M=M+1
@SP
AM=M-1
D=M
M=0
A=A-1
M=M+D
@112
D=A
@SP
A=M
M=D
@SP
M=M+1
@SP
AM=M-1
D=M
M=0
A=A-1
M=M-D
@SP
A=M-1
M=-M
@SP
AM=M-1
D=M
M=0
A=A-1
M=M&D
@82
D=A
@SP
A=M
M=D
@SP
M=M+1
@SP
AM=M-1
D=M
M=0
A=A-1
M=M|D
@SP
A=M-1
M=!M
@END
0;JMP
(TRUE)
@SP
A=M-1
M=-1
@address
A=M
0;JMP
(FALSE)
@SP
A=M-1
M=0
@address
A=M
0;JMP
(END)
@END
0;JMP
