@256
D=A
@SP
M=D
@return-address0
D=A
@SP
A=M
M=D
@SP
M=M+1
@LCL
D=M
@SP
A=M
M=D
@SP
M=M+1
@ARG
D=M
@SP
A=M
M=D
@SP
M=M+1
@THIS
D=M
@SP
A=M
M=D
@SP
M=M+1
@THAT
D=M
@SP
A=M
M=D
@SP
M=M+1
@SP
D=M
@0
D=D-A
@5
D=D-A
@ARG
M=D
@SP
D=M
@LCL
M=D
@Sys.init
0;JMP
(return-address0)
(Sys.init)
@4000
D=A
@SP
A=M
M=D
@SP
M=M+1
@3
D=A
@0
D=D+A
@13
M=D
@SP
AM=M-1
D=M
M=0
@13
A=M
M=D
@5000
D=A
@SP
A=M
M=D
@SP
M=M+1
@3
D=A
@1
D=D+A
@13
M=D
@SP
AM=M-1
D=M
M=0
@13
A=M
M=D
@return-address1
D=A
@SP
A=M
M=D
@SP
M=M+1
@LCL
D=M
@SP
A=M
M=D
@SP
M=M+1
@ARG
D=M
@SP
A=M
M=D
@SP
M=M+1
@THIS
D=M
@SP
A=M
M=D
@SP
M=M+1
@THAT
D=M
@SP
A=M
M=D
@SP
M=M+1
@SP
D=M
@0
D=D-A
@5
D=D-A
@ARG
M=D
@SP
D=M
@LCL
M=D
@Sys.main
0;JMP
(return-address1)
@5
D=A
@1
D=D+A
@13
M=D
@SP
AM=M-1
D=M
M=0
@13
A=M
M=D
(Sys.init$LOOP)
@Sys.init$LOOP
0;JMP
(Sys.main)
@SP
D=M
M=M+1
A=D
M=0
@SP
D=M
M=M+1
A=D
M=0
@SP
D=M
M=M+1
A=D
M=0
@SP
D=M
M=M+1
A=D
M=0
@SP
D=M
M=M+1
A=D
M=0
@4001
D=A
@SP
A=M
M=D
@SP
M=M+1
@3
D=A
@0
D=D+A
@13
M=D
@SP
AM=M-1
D=M
M=0
@13
A=M
M=D
@5001
D=A
@SP
A=M
M=D
@SP
M=M+1
@3
D=A
@1
D=D+A
@13
M=D
@SP
AM=M-1
D=M
M=0
@13
A=M
M=D
@200
D=A
@SP
A=M
M=D
@SP
M=M+1
@LCL
D=M
@1
D=D+A
@13
M=D
@SP
AM=M-1
D=M
M=0
@13
A=M
M=D
@40
D=A
@SP
A=M
M=D
@SP
M=M+1
@LCL
D=M
@2
D=D+A
@13
M=D
@SP
AM=M-1
D=M
M=0
@13
A=M
M=D
@6
D=A
@SP
A=M
M=D
@SP
M=M+1
@LCL
D=M
@3
D=D+A
@13
M=D
@SP
AM=M-1
D=M
M=0
@13
A=M
M=D
@123
D=A
@SP
A=M
M=D
@SP
M=M+1
@return-address2
D=A
@SP
A=M
M=D
@SP
M=M+1
@LCL
D=M
@SP
A=M
M=D
@SP
M=M+1
@ARG
D=M
@SP
A=M
M=D
@SP
M=M+1
@THIS
D=M
@SP
A=M
M=D
@SP
M=M+1
@THAT
D=M
@SP
A=M
M=D
@SP
M=M+1
@SP
D=M
@1
D=D-A
@5
D=D-A
@ARG
M=D
@SP
D=M
@LCL
M=D
@Sys.add12
0;JMP
(return-address2)
@5
D=A
@0
D=D+A
@13
M=D
@SP
AM=M-1
D=M
M=0
@13
A=M
M=D
@LCL
D=M
@0
A=D+A
D=M
@SP
A=M
M=D
@SP
M=M+1
@LCL
D=M
@1
A=D+A
D=M
@SP
A=M
M=D
@SP
M=M+1
@LCL
D=M
@2
A=D+A
D=M
@SP
A=M
M=D
@SP
M=M+1
@LCL
D=M
@3
A=D+A
D=M
@SP
A=M
M=D
@SP
M=M+1
@LCL
D=M
@4
A=D+A
D=M
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
M=D+M
@SP
AM=M-1
D=M
M=0
A=A-1
M=D+M
@SP
AM=M-1
D=M
M=0
A=A-1
M=D+M
@SP
AM=M-1
D=M
M=0
A=A-1
M=D+M
@LCL
D=M
@14
M=D
@5
A=D-A
D=M
@15
M=D
@SP
AM=M-1
D=M
M=0
@ARG
A=M
M=D
D=A
@SP
M=D+1
@14
D=M
@1
A=D-A
D=M
@THAT
M=D
@14
D=M
@2
A=D-A
D=M
@THIS
M=D
@14
D=M
@3
A=D-A
D=M
@ARG
M=D
@14
D=M
@4
A=D-A
D=M
@LCL
M=D
@15
A=M
0;JMP
(Sys.add12)
@4002
D=A
@SP
A=M
M=D
@SP
M=M+1
@3
D=A
@0
D=D+A
@13
M=D
@SP
AM=M-1
D=M
M=0
@13
A=M
M=D
@5002
D=A
@SP
A=M
M=D
@SP
M=M+1
@3
D=A
@1
D=D+A
@13
M=D
@SP
AM=M-1
D=M
M=0
@13
A=M
M=D
@ARG
D=M
@0
A=D+A
D=M
@SP
A=M
M=D
@SP
M=M+1
@12
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
M=D+M
@LCL
D=M
@14
M=D
@5
A=D-A
D=M
@15
M=D
@SP
AM=M-1
D=M
M=0
@ARG
A=M
M=D
D=A
@SP
M=D+1
@14
D=M
@1
A=D-A
D=M
@THAT
M=D
@14
D=M
@2
A=D-A
D=M
@THIS
M=D
@14
D=M
@3
A=D-A
D=M
@ARG
M=D
@14
D=M
@4
A=D-A
D=M
@LCL
M=D
@15
A=M
0;JMP
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
