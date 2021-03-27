	.text
	.file	"builtin_function.c"
	.globl	print                   # -- Begin function print
	.p2align	2
print:                                  # @print
# %bb.0:
	lui	a1, %hi(.L.str)
	addi	a1, a1, %lo(.L.str)
	mv	a2, a0
	mv	a0, a1
	mv	a1, a2
	tail	printf
.Lfunc_end0:
                                        # -- End function
	.globl	println                 # -- Begin function println
	.p2align	2
println:                                # @println
# %bb.0:
	tail	puts
.Lfunc_end1:
                                        # -- End function
	.globl	printInt                # -- Begin function printInt
	.p2align	2
printInt:                               # @printInt
# %bb.0:
	lui	a1, %hi(.L.str.2)
	addi	a1, a1, %lo(.L.str.2)
	mv	a2, a0
	mv	a0, a1
	mv	a1, a2
	tail	printf
.Lfunc_end2:
                                        # -- End function
	.globl	printlnInt              # -- Begin function printlnInt
	.p2align	2
printlnInt:                             # @printlnInt
# %bb.0:
	lui	a1, %hi(.L.str.3)
	addi	a1, a1, %lo(.L.str.3)
	mv	a2, a0
	mv	a0, a1
	mv	a1, a2
	tail	printf
.Lfunc_end3:
                                        # -- End function
	.globl	getString               # -- Begin function getString
	.p2align	2
getString:                              # @getString
# %bb.0:
	addi	sp, sp, -16
	sw	ra, 12(sp)
	sw	s0, 8(sp)
	addi	a0, zero, 257
	mv	a1, zero
	call	malloc
	mv	s0, a0
	lui	a0, %hi(.L.str)
	addi	a0, a0, %lo(.L.str)
	mv	a1, s0
	call	__isoc99_scanf
	mv	a0, s0
	lw	s0, 8(sp)
	lw	ra, 12(sp)
	addi	sp, sp, 16
	ret
.Lfunc_end4:
                                        # -- End function
	.globl	getInt                  # -- Begin function getInt
	.p2align	2
getInt:                                 # @getInt
# %bb.0:
	addi	sp, sp, -16
	sw	ra, 12(sp)
	lui	a0, %hi(.L.str.2)
	addi	a0, a0, %lo(.L.str.2)
	addi	a1, sp, 8
	call	__isoc99_scanf
	lw	a0, 8(sp)
	lw	ra, 12(sp)
	addi	sp, sp, 16
	ret
.Lfunc_end5:
                                        # -- End function
	.globl	_string_less            # -- Begin function _string_less
	.p2align	2
_string_less:                           # @_string_less
# %bb.0:
	addi	sp, sp, -16
	sw	ra, 12(sp)
	call	strcmp
	srli	a0, a0, 31
	lw	ra, 12(sp)
	addi	sp, sp, 16
	ret
.Lfunc_end6:
                                        # -- End function
	.globl	_string_greater         # -- Begin function _string_greater
	.p2align	2
_string_greater:                        # @_string_greater
# %bb.0:
	addi	sp, sp, -16
	sw	ra, 12(sp)
	call	strcmp
	sgtz	a0, a0
	lw	ra, 12(sp)
	addi	sp, sp, 16
	ret
.Lfunc_end7:
                                        # -- End function
	.globl	_string_lessEqual       # -- Begin function _string_lessEqual
	.p2align	2
_string_lessEqual:                      # @_string_lessEqual
# %bb.0:
	addi	sp, sp, -16
	sw	ra, 12(sp)
	call	strcmp
	slti	a0, a0, 1
	lw	ra, 12(sp)
	addi	sp, sp, 16
	ret
.Lfunc_end8:
                                        # -- End function
	.globl	_string_greaterEqual    # -- Begin function _string_greaterEqual
	.p2align	2
_string_greaterEqual:                   # @_string_greaterEqual
# %bb.0:
	addi	sp, sp, -16
	sw	ra, 12(sp)
	call	strcmp
	not	a0, a0
	srli	a0, a0, 31
	lw	ra, 12(sp)
	addi	sp, sp, 16
	ret
.Lfunc_end9:
                                        # -- End function
	.globl	_string_equal           # -- Begin function _string_equal
	.p2align	2
_string_equal:                          # @_string_equal
# %bb.0:
	addi	sp, sp, -16
	sw	ra, 12(sp)
	call	strcmp
	seqz	a0, a0
	lw	ra, 12(sp)
	addi	sp, sp, 16
	ret
.Lfunc_end10:
                                        # -- End function
	.globl	_string_notEqual        # -- Begin function _string_notEqual
	.p2align	2
_string_notEqual:                       # @_string_notEqual
# %bb.0:
	addi	sp, sp, -16
	sw	ra, 12(sp)
	call	strcmp
	snez	a0, a0
	lw	ra, 12(sp)
	addi	sp, sp, 16
	ret
.Lfunc_end11:
                                        # -- End function
	.globl	_string_length          # -- Begin function _string_length
	.p2align	2
_string_length:                         # @_string_length
# %bb.0:
	addi	sp, sp, -16
	sw	ra, 12(sp)
	call	strlen
	lw	ra, 12(sp)
	addi	sp, sp, 16
	ret
.Lfunc_end12:
                                        # -- End function
	.globl	toString                # -- Begin function toString
	.p2align	2
toString:                               # @toString
# %bb.0:
	addi	sp, sp, -48
	sw	ra, 44(sp)
	sw	s0, 40(sp)
	sw	s1, 36(sp)
	sw	s2, 32(sp)
	sw	s3, 28(sp)
	sw	s4, 24(sp)
	sw	s5, 20(sp)
	beqz	a0, .LBB13_4
# %bb.1:
	mv	s5, a0
	srai	a0, a0, 31
	add	a1, s5, a0
	xor	s0, a1, a0
	addi	a1, zero, 1
	slti	s3, s5, 1
	blt	s0, a1, .LBB13_5
# %bb.2:
	mv	a5, zero
	addi	a6, sp, 10
	lui	a2, 838861
	addi	a2, a2, -819
	addi	a3, zero, 10
	addi	a4, zero, 9
.LBB13_3:                               # =>This Inner Loop Header: Depth=1
	mv	a0, s0
	addi	s1, a5, 1
	slli	a5, a5, 24
	srai	a5, a5, 24
	add	a5, a6, a5
	mulhu	s0, s0, a2
	srli	s0, s0, 3
	mul	a1, s0, a3
	sub	a1, a0, a1
	sb	a1, 0(a5)
	mv	a5, s1
	bltu	a4, a0, .LBB13_3
	j	.LBB13_6
.LBB13_4:
	addi	a0, zero, 2
	mv	a1, zero
	call	malloc
	addi	a1, zero, 48
	sb	a1, 0(a0)
	sb	zero, 1(a0)
	j	.LBB13_20
.LBB13_5:
	mv	s1, zero
.LBB13_6:
	slli	a0, s1, 24
	srai	s4, a0, 24
	add	s2, s4, s3
	addi	a0, s2, 1
	srai	a1, a0, 31
	call	malloc
	bgtz	s5, .LBB13_8
# %bb.7:
	addi	a1, zero, 45
	sb	a1, 0(a0)
.LBB13_8:
	addi	a1, zero, 1
	blt	s4, a1, .LBB13_19
# %bb.9:
	andi	t0, s1, 255
	addi	a1, zero, 31
	addi	t1, s4, -1
	bltu	a1, t0, .LBB13_11
# %bb.10:
	mv	s4, zero
	mv	a1, zero
	j	.LBB13_17
.LBB13_11:
	andi	s4, s1, 224
	addi	a2, s4, -32
	sltu	a1, a2, s4
	addi	a1, a1, -1
	slli	a4, a1, 27
	srli	a5, a2, 5
	or	s1, a5, a4
	addi	a4, s1, 1
	or	a2, a2, a1
	andi	a6, a4, 1
	beqz	a2, .LBB13_21
# %bb.12:
	mv	t6, zero
	mv	t5, zero
	srli	a1, a1, 5
	sltu	s1, a4, s1
	add	s1, a1, s1
	sub	t3, a4, a6
	sltu	a4, a4, a6
	sub	t2, s1, a4
	addi	a7, sp, 10
.LBB13_13:                              # =>This Inner Loop Header: Depth=1
	sub	a4, t1, t6
	add	s1, a7, a4
	lb	a1, -15(s1)
	lb	s0, -14(s1)
	lb	t4, -13(s1)
	addi	a1, a1, 48
	or	a4, t6, s3
	add	a4, a0, a4
	sb	a1, 15(a4)
	lb	a1, -12(s1)
	addi	s0, s0, 48
	sb	s0, 14(a4)
	lb	s0, -11(s1)
	addi	a5, t4, 48
	sb	a5, 13(a4)
	lb	a5, -10(s1)
	addi	a1, a1, 48
	sb	a1, 12(a4)
	lb	a1, -9(s1)
	addi	s0, s0, 48
	sb	s0, 11(a4)
	lb	s0, -8(s1)
	addi	a5, a5, 48
	sb	a5, 10(a4)
	lb	a5, -7(s1)
	addi	a1, a1, 48
	sb	a1, 9(a4)
	lb	a1, -6(s1)
	addi	s0, s0, 48
	sb	s0, 8(a4)
	lb	s0, -5(s1)
	addi	a5, a5, 48
	sb	a5, 7(a4)
	lb	a5, -4(s1)
	addi	a1, a1, 48
	sb	a1, 6(a4)
	lb	a1, -3(s1)
	addi	s0, s0, 48
	sb	s0, 5(a4)
	lb	s0, -2(s1)
	addi	a5, a5, 48
	sb	a5, 4(a4)
	lb	a5, -1(s1)
	addi	a1, a1, 48
	sb	a1, 3(a4)
	lb	a1, 0(s1)
	addi	s0, s0, 48
	sb	s0, 2(a4)
	lb	s0, -31(s1)
	addi	a5, a5, 48
	sb	a5, 1(a4)
	lb	a5, -30(s1)
	addi	a1, a1, 48
	sb	a1, 0(a4)
	lb	a1, -29(s1)
	addi	s0, s0, 48
	sb	s0, 31(a4)
	lb	s0, -28(s1)
	addi	a5, a5, 48
	sb	a5, 30(a4)
	lb	a5, -27(s1)
	addi	a1, a1, 48
	sb	a1, 29(a4)
	lb	a1, -26(s1)
	addi	s0, s0, 48
	sb	s0, 28(a4)
	lb	s0, -25(s1)
	addi	a5, a5, 48
	sb	a5, 27(a4)
	lb	a5, -24(s1)
	addi	a1, a1, 48
	sb	a1, 26(a4)
	lb	a1, -23(s1)
	addi	s0, s0, 48
	sb	s0, 25(a4)
	lb	s0, -22(s1)
	addi	a5, a5, 48
	sb	a5, 24(a4)
	lb	a5, -21(s1)
	addi	a1, a1, 48
	sb	a1, 23(a4)
	lb	a1, -20(s1)
	addi	s0, s0, 48
	sb	s0, 22(a4)
	lb	s0, -18(s1)
	addi	a5, a5, 48
	sb	a5, 21(a4)
	lb	a5, -17(s1)
	addi	a1, a1, 48
	sb	a1, 20(a4)
	lb	a1, -19(s1)
	lb	a2, -16(s1)
	addi	a5, a5, 48
	addi	s1, s0, 48
	addi	a1, a1, 48
	sb	a1, 19(a4)
	sb	s1, 18(a4)
	sb	a5, 17(a4)
	ori	s0, t6, 32
	sub	a1, t1, s0
	add	s1, a7, a1
	lb	a1, -15(s1)
	addi	a2, a2, 48
	sb	a2, 16(a4)
	lb	a4, -14(s1)
	addi	a1, a1, 48
	or	a2, s0, s3
	add	a2, a0, a2
	sb	a1, 15(a2)
	lb	a1, -13(s1)
	addi	a4, a4, 48
	sb	a4, 14(a2)
	lb	a4, -12(s1)
	addi	a1, a1, 48
	sb	a1, 13(a2)
	lb	a1, -11(s1)
	addi	a4, a4, 48
	sb	a4, 12(a2)
	lb	a4, -10(s1)
	addi	a1, a1, 48
	sb	a1, 11(a2)
	lb	a1, -9(s1)
	addi	a4, a4, 48
	sb	a4, 10(a2)
	lb	a4, -8(s1)
	addi	a1, a1, 48
	sb	a1, 9(a2)
	lb	a1, -7(s1)
	addi	a4, a4, 48
	sb	a4, 8(a2)
	lb	a4, -6(s1)
	addi	a1, a1, 48
	sb	a1, 7(a2)
	lb	a1, -5(s1)
	addi	a4, a4, 48
	sb	a4, 6(a2)
	lb	a4, -4(s1)
	addi	a1, a1, 48
	sb	a1, 5(a2)
	lb	a1, -3(s1)
	addi	a4, a4, 48
	sb	a4, 4(a2)
	lb	a4, -2(s1)
	addi	a1, a1, 48
	sb	a1, 3(a2)
	lb	a1, -1(s1)
	addi	a4, a4, 48
	sb	a4, 2(a2)
	lb	a4, 0(s1)
	addi	a1, a1, 48
	sb	a1, 1(a2)
	lb	a1, -31(s1)
	addi	a4, a4, 48
	sb	a4, 0(a2)
	lb	a4, -30(s1)
	addi	a1, a1, 48
	sb	a1, 31(a2)
	lb	a1, -29(s1)
	addi	a4, a4, 48
	sb	a4, 30(a2)
	lb	a4, -28(s1)
	addi	a1, a1, 48
	sb	a1, 29(a2)
	lb	a1, -27(s1)
	addi	a4, a4, 48
	sb	a4, 28(a2)
	lb	a4, -26(s1)
	addi	a1, a1, 48
	sb	a1, 27(a2)
	lb	a1, -25(s1)
	addi	a4, a4, 48
	sb	a4, 26(a2)
	lb	a4, -24(s1)
	addi	a1, a1, 48
	sb	a1, 25(a2)
	lb	a1, -23(s1)
	addi	a4, a4, 48
	sb	a4, 24(a2)
	lb	a4, -22(s1)
	addi	a1, a1, 48
	sb	a1, 23(a2)
	lb	a1, -21(s1)
	addi	a4, a4, 48
	sb	a4, 22(a2)
	lb	a4, -20(s1)
	addi	a1, a1, 48
	sb	a1, 21(a2)
	lb	a1, -19(s1)
	addi	a4, a4, 48
	sb	a4, 20(a2)
	lb	a4, -18(s1)
	addi	a1, a1, 48
	sb	a1, 19(a2)
	lb	a1, -17(s1)
	lb	a5, -16(s1)
	addi	a4, a4, 48
	sb	a4, 18(a2)
	addi	a1, a1, 48
	sb	a1, 17(a2)
	addi	a1, a5, 48
	sb	a1, 16(a2)
	addi	s0, t6, 64
	sltu	a1, s0, t6
	addi	a2, t3, -2
	sltu	a4, a2, t3
	add	a4, t2, a4
	addi	t2, a4, -1
	or	a4, a2, t2
	add	t5, t5, a1
	mv	t6, s0
	mv	t3, a2
	bnez	a4, .LBB13_13
# %bb.14:
	beqz	a6, .LBB13_16
.LBB13_15:
	sub	a1, t1, s0
	addi	a2, sp, 10
	add	a1, a2, a1
	lb	a4, -15(a1)
	lb	a2, -14(a1)
	addi	a4, a4, 48
	or	a5, s0, s3
	add	a5, a0, a5
	sb	a4, 15(a5)
	lb	a4, -13(a1)
	addi	a2, a2, 48
	sb	a2, 14(a5)
	lb	a2, -12(a1)
	addi	a4, a4, 48
	sb	a4, 13(a5)
	lb	a4, -11(a1)
	addi	a2, a2, 48
	sb	a2, 12(a5)
	lb	a2, -10(a1)
	addi	a4, a4, 48
	sb	a4, 11(a5)
	lb	a4, -9(a1)
	addi	a2, a2, 48
	sb	a2, 10(a5)
	lb	a2, -8(a1)
	addi	a4, a4, 48
	sb	a4, 9(a5)
	lb	a4, -7(a1)
	addi	a2, a2, 48
	sb	a2, 8(a5)
	lb	a2, -6(a1)
	addi	a4, a4, 48
	sb	a4, 7(a5)
	lb	a4, -5(a1)
	addi	a2, a2, 48
	sb	a2, 6(a5)
	lb	a2, -4(a1)
	addi	a4, a4, 48
	sb	a4, 5(a5)
	lb	a4, -3(a1)
	addi	a2, a2, 48
	sb	a2, 4(a5)
	lb	a2, -2(a1)
	addi	a4, a4, 48
	sb	a4, 3(a5)
	lb	a4, -1(a1)
	addi	a2, a2, 48
	sb	a2, 2(a5)
	lb	a2, 0(a1)
	addi	a4, a4, 48
	sb	a4, 1(a5)
	lb	a4, -31(a1)
	addi	a2, a2, 48
	sb	a2, 0(a5)
	lb	a2, -30(a1)
	addi	a4, a4, 48
	sb	a4, 31(a5)
	lb	a4, -29(a1)
	addi	a2, a2, 48
	sb	a2, 30(a5)
	lb	a2, -28(a1)
	addi	a4, a4, 48
	sb	a4, 29(a5)
	lb	a4, -27(a1)
	addi	a2, a2, 48
	sb	a2, 28(a5)
	lb	a2, -26(a1)
	addi	a4, a4, 48
	sb	a4, 27(a5)
	lb	a4, -25(a1)
	addi	a2, a2, 48
	sb	a2, 26(a5)
	lb	a2, -24(a1)
	addi	a4, a4, 48
	sb	a4, 25(a5)
	lb	a4, -23(a1)
	addi	a2, a2, 48
	sb	a2, 24(a5)
	lb	a2, -22(a1)
	addi	a4, a4, 48
	sb	a4, 23(a5)
	lb	a4, -21(a1)
	addi	a2, a2, 48
	sb	a2, 22(a5)
	lb	a2, -20(a1)
	addi	a4, a4, 48
	sb	a4, 21(a5)
	lb	a4, -19(a1)
	addi	a2, a2, 48
	sb	a2, 20(a5)
	lb	a2, -18(a1)
	addi	a4, a4, 48
	sb	a4, 19(a5)
	lb	a4, -17(a1)
	lb	a1, -16(a1)
	addi	a2, a2, 48
	sb	a2, 18(a5)
	addi	a2, a4, 48
	sb	a2, 17(a5)
	addi	a1, a1, 48
	sb	a1, 16(a5)
.LBB13_16:
	xor	a2, s4, t0
	mv	a1, zero
	beqz	a2, .LBB13_19
.LBB13_17:
	addi	a2, sp, 10
.LBB13_18:                              # =>This Inner Loop Header: Depth=1
	sub	a4, t1, s4
	add	a4, a2, a4
	lb	a4, 0(a4)
	addi	a4, a4, 48
	add	a5, s4, s3
	add	a5, a0, a5
	addi	a3, s4, 1
	sltu	s1, a3, s4
	add	a1, a1, s1
	xor	s1, a3, t0
	or	s1, s1, a1
	sb	a4, 0(a5)
	mv	s4, a3
	bnez	s1, .LBB13_18
.LBB13_19:
	add	a1, a0, s2
	sb	zero, 0(a1)
.LBB13_20:
	lw	s5, 20(sp)
	lw	s4, 24(sp)
	lw	s3, 28(sp)
	lw	s2, 32(sp)
	lw	s1, 36(sp)
	lw	s0, 40(sp)
	lw	ra, 44(sp)
	addi	sp, sp, 48
	ret
.LBB13_21:
	mv	s0, zero
	bnez	a6, .LBB13_15
	j	.LBB13_16
.Lfunc_end13:
                                        # -- End function
	.globl	_string_substring       # -- Begin function _string_substring
	.p2align	2
_string_substring:                      # @_string_substring
# %bb.0:
	addi	sp, sp, -112
	sw	ra, 108(sp)
	sw	s0, 104(sp)
	sw	s1, 100(sp)
	sw	s2, 96(sp)
	sw	s3, 92(sp)
	sw	s4, 88(sp)
	sw	s5, 84(sp)
	sw	s6, 80(sp)
	sw	s7, 76(sp)
	sw	s8, 72(sp)
	sw	s9, 68(sp)
	sw	s10, 64(sp)
	sw	s11, 60(sp)
	mv	s2, a2
	mv	s0, a1
	mv	s5, a0
	sub	s1, a2, a1
	addi	a0, s1, 1
	srai	a1, a0, 31
	call	malloc
	addi	a1, zero, 1
	blt	s1, a1, .LBB14_14
# %bb.1:
	addi	a1, zero, 31
	bltu	a1, s1, .LBB14_3
# %bb.2:
	mv	a1, zero
	j	.LBB14_12
.LBB14_3:
	addi	a2, s2, -1
	sub	a3, a2, s0
	addi	a1, a3, 1
	slli	a1, a1, 24
	srai	a4, a1, 24
	addi	a5, zero, 1
	mv	a1, zero
	blt	a4, a5, .LBB14_12
# %bb.4:
	addi	a4, zero, 127
	bltu	a4, a3, .LBB14_12
# %bb.5:
	blt	a2, s0, .LBB14_12
# %bb.6:
	sw	a0, 16(sp)
	sw	s1, 4(sp)
	andi	a1, s1, -32
	addi	a2, a1, -32
	srli	a3, a2, 5
	addi	a4, a3, 1
	andi	a0, a4, 1
	sw	a0, 0(sp)
	mv	ra, zero
	beqz	a2, .LBB14_9
# %bb.7:
	sub	s4, a0, a4
	sw	s0, 12(sp)
	sw	s5, 8(sp)
.LBB14_8:                               # =>This Inner Loop Header: Depth=1
	add	a5, s0, ra
	add	a5, s5, a5
	lb	a0, 0(a5)
	sw	a0, 56(sp)
	lb	a0, 1(a5)
	sw	a0, 48(sp)
	lb	a0, 2(a5)
	sw	a0, 40(sp)
	lb	a0, 3(a5)
	sw	a0, 32(sp)
	lb	a0, 4(a5)
	sw	a0, 24(sp)
	lb	t4, 5(a5)
	lb	t5, 6(a5)
	lb	t6, 7(a5)
	lb	s2, 8(a5)
	lb	s6, 9(a5)
	lb	s7, 10(a5)
	lb	s8, 11(a5)
	lb	s9, 12(a5)
	lb	s1, 13(a5)
	lb	s0, 14(a5)
	lb	a3, 15(a5)
	lb	a0, 16(a5)
	sw	a0, 52(sp)
	lb	a0, 17(a5)
	sw	a0, 44(sp)
	lb	a0, 18(a5)
	sw	a0, 36(sp)
	lb	a0, 19(a5)
	sw	a0, 28(sp)
	lb	a0, 20(a5)
	sw	a0, 20(sp)
	lb	s3, 21(a5)
	lb	t3, 22(a5)
	lb	t0, 23(a5)
	lb	a7, 24(a5)
	lb	a6, 25(a5)
	slli	s10, ra, 24
	srai	a0, s10, 24
	lw	s5, 16(sp)
	add	a0, s5, a0
	lb	s10, 26(a5)
	lb	a2, 27(a5)
	lb	a4, 28(a5)
	lb	s11, 29(a5)
	lb	t1, 30(a5)
	lb	t2, 31(a5)
	sb	a3, 15(a0)
	sb	s0, 14(a0)
	sb	s1, 13(a0)
	sb	s9, 12(a0)
	sb	s8, 11(a0)
	sb	s7, 10(a0)
	sb	s6, 9(a0)
	sb	s2, 8(a0)
	sb	t6, 7(a0)
	sb	t5, 6(a0)
	sb	t4, 5(a0)
	lw	a3, 24(sp)
	sb	a3, 4(a0)
	lw	a3, 32(sp)
	sb	a3, 3(a0)
	lw	a3, 40(sp)
	sb	a3, 2(a0)
	lw	a3, 48(sp)
	sb	a3, 1(a0)
	lw	a3, 56(sp)
	sb	a3, 0(a0)
	sb	t2, 31(a0)
	sb	t1, 30(a0)
	sb	s11, 29(a0)
	sb	a4, 28(a0)
	sb	a2, 27(a0)
	sb	s10, 26(a0)
	sb	a6, 25(a0)
	sb	a7, 24(a0)
	sb	t0, 23(a0)
	sb	t3, 22(a0)
	sb	s3, 21(a0)
	lw	a2, 20(sp)
	sb	a2, 20(a0)
	lw	a2, 28(sp)
	sb	a2, 19(a0)
	lw	a2, 36(sp)
	sb	a2, 18(a0)
	lw	a2, 44(sp)
	sb	a2, 17(a0)
	lw	a2, 52(sp)
	sb	a2, 16(a0)
	lb	a0, 32(a5)
	sw	a0, 56(sp)
	lb	a0, 33(a5)
	sw	a0, 48(sp)
	lb	a0, 34(a5)
	sw	a0, 40(sp)
	lb	a0, 35(a5)
	sw	a0, 32(sp)
	lb	a0, 36(a5)
	sw	a0, 24(sp)
	lb	s8, 37(a5)
	lb	s6, 38(a5)
	lb	t6, 39(a5)
	lb	s2, 40(a5)
	lb	s3, 41(a5)
	lb	t2, 42(a5)
	lb	s0, 43(a5)
	lb	a7, 44(a5)
	lb	a2, 45(a5)
	lb	a3, 46(a5)
	lb	a4, 47(a5)
	lb	a0, 48(a5)
	sw	a0, 52(sp)
	lb	a0, 49(a5)
	sw	a0, 44(sp)
	lb	a0, 50(a5)
	sw	a0, 36(sp)
	lb	a0, 51(a5)
	sw	a0, 28(sp)
	lb	a0, 52(a5)
	sw	a0, 20(sp)
	lb	s9, 53(a5)
	lb	s10, 54(a5)
	lb	s11, 55(a5)
	lb	t5, 56(a5)
	lb	t4, 57(a5)
	lb	t1, 58(a5)
	lb	t0, 59(a5)
	lb	a6, 60(a5)
	lb	a0, 61(a5)
	lb	t3, 62(a5)
	lb	a5, 63(a5)
	addi	s7, ra, 32
	slli	s1, s7, 24
	srai	s1, s1, 24
	add	s1, s5, s1
	sb	a4, 15(s1)
	sb	a3, 14(s1)
	sb	a2, 13(s1)
	sb	a7, 12(s1)
	sb	s0, 11(s1)
	lw	s0, 12(sp)
	sb	t2, 10(s1)
	sb	s3, 9(s1)
	lw	s5, 8(sp)
	sb	s2, 8(s1)
	sb	t6, 7(s1)
	sb	s6, 6(s1)
	sb	s8, 5(s1)
	lw	a2, 24(sp)
	sb	a2, 4(s1)
	lw	a2, 32(sp)
	sb	a2, 3(s1)
	lw	a2, 40(sp)
	sb	a2, 2(s1)
	lw	a2, 48(sp)
	sb	a2, 1(s1)
	lw	a2, 56(sp)
	sb	a2, 0(s1)
	sb	a5, 31(s1)
	sb	t3, 30(s1)
	sb	a0, 29(s1)
	sb	a6, 28(s1)
	sb	t0, 27(s1)
	sb	t1, 26(s1)
	sb	t4, 25(s1)
	sb	t5, 24(s1)
	sb	s11, 23(s1)
	sb	s10, 22(s1)
	sb	s9, 21(s1)
	lw	a0, 20(sp)
	sb	a0, 20(s1)
	lw	a0, 28(sp)
	sb	a0, 19(s1)
	lw	a0, 36(sp)
	sb	a0, 18(s1)
	lw	a0, 44(sp)
	sb	a0, 17(s1)
	lw	a0, 52(sp)
	sb	a0, 16(s1)
	addi	s4, s4, 2
	addi	ra, ra, 64
	bnez	s4, .LBB14_8
.LBB14_9:
	lw	a0, 0(sp)
	beqz	a0, .LBB14_11
# %bb.10:
	add	a0, ra, s0
	add	a0, s5, a0
	lb	a2, 0(a0)
	sw	a2, 56(sp)
	lb	a2, 1(a0)
	sw	a2, 48(sp)
	lb	a2, 2(a0)
	sw	a2, 40(sp)
	lb	a2, 3(a0)
	sw	a2, 32(sp)
	lb	a2, 4(a0)
	sw	a2, 24(sp)
	lb	s7, 5(a0)
	lb	s6, 6(a0)
	lb	s4, 7(a0)
	lb	s3, 8(a0)
	lb	s2, 9(a0)
	lb	t5, 10(a0)
	mv	t3, s0
	lb	t2, 11(a0)
	lb	a7, 12(a0)
	lb	a3, 13(a0)
	lb	a4, 14(a0)
	lb	a5, 15(a0)
	lb	a2, 16(a0)
	sw	a2, 52(sp)
	lb	a2, 17(a0)
	sw	a2, 44(sp)
	lb	a2, 18(a0)
	sw	a2, 36(sp)
	lb	a2, 19(a0)
	sw	a2, 28(sp)
	lb	a2, 20(a0)
	sw	a2, 20(sp)
	lb	s8, 21(a0)
	lb	s9, 22(a0)
	lb	s10, 23(a0)
	lb	s11, 24(a0)
	lb	t6, 25(a0)
	lb	t4, 26(a0)
	lb	t1, 27(a0)
	lb	t0, 28(a0)
	lb	a6, 29(a0)
	lb	a2, 30(a0)
	lb	a0, 31(a0)
	slli	s1, ra, 24
	srai	s1, s1, 24
	lw	s0, 16(sp)
	add	s1, s0, s1
	sb	a5, 15(s1)
	sb	a4, 14(s1)
	sb	a3, 13(s1)
	sb	a7, 12(s1)
	sb	t2, 11(s1)
	mv	s0, t3
	sb	t5, 10(s1)
	sb	s2, 9(s1)
	sb	s3, 8(s1)
	sb	s4, 7(s1)
	sb	s6, 6(s1)
	sb	s7, 5(s1)
	lw	a3, 24(sp)
	sb	a3, 4(s1)
	lw	a3, 32(sp)
	sb	a3, 3(s1)
	lw	a3, 40(sp)
	sb	a3, 2(s1)
	lw	a3, 48(sp)
	sb	a3, 1(s1)
	lw	a3, 56(sp)
	sb	a3, 0(s1)
	sb	a0, 31(s1)
	sb	a2, 30(s1)
	sb	a6, 29(s1)
	sb	t0, 28(s1)
	sb	t1, 27(s1)
	sb	t4, 26(s1)
	sb	t6, 25(s1)
	sb	s11, 24(s1)
	sb	s10, 23(s1)
	sb	s9, 22(s1)
	sb	s8, 21(s1)
	lw	a0, 20(sp)
	sb	a0, 20(s1)
	lw	a0, 28(sp)
	sb	a0, 19(s1)
	lw	a0, 36(sp)
	sb	a0, 18(s1)
	lw	a0, 44(sp)
	sb	a0, 17(s1)
	lw	a0, 52(sp)
	sb	a0, 16(s1)
.LBB14_11:
	lw	s1, 4(sp)
	lw	a0, 16(sp)
	beq	s1, a1, .LBB14_14
.LBB14_12:
	mv	a2, a1
.LBB14_13:                              # =>This Inner Loop Header: Depth=1
	add	a1, a1, s0
	add	a1, s5, a1
	lb	a4, 0(a1)
	slli	a1, a2, 24
	srai	a1, a1, 24
	add	a3, a0, a1
	addi	a2, a2, 1
	slli	a1, a2, 24
	srai	a1, a1, 24
	sb	a4, 0(a3)
	blt	a1, s1, .LBB14_13
.LBB14_14:
	add	a1, a0, s1
	sb	zero, 0(a1)
	lw	s11, 60(sp)
	lw	s10, 64(sp)
	lw	s9, 68(sp)
	lw	s8, 72(sp)
	lw	s7, 76(sp)
	lw	s6, 80(sp)
	lw	s5, 84(sp)
	lw	s4, 88(sp)
	lw	s3, 92(sp)
	lw	s2, 96(sp)
	lw	s1, 100(sp)
	lw	s0, 104(sp)
	lw	ra, 108(sp)
	addi	sp, sp, 112
	ret
.Lfunc_end14:
                                        # -- End function
	.globl	_string_ord             # -- Begin function _string_ord
	.p2align	2
_string_ord:                            # @_string_ord
# %bb.0:
	add	a0, a0, a1
	lb	a0, 0(a0)
	ret
.Lfunc_end15:
                                        # -- End function
	.globl	_string_concatenate     # -- Begin function _string_concatenate
	.p2align	2
_string_concatenate:                    # @_string_concatenate
# %bb.0:
	addi	sp, sp, -32
	sw	ra, 28(sp)
	sw	s0, 24(sp)
	sw	s1, 20(sp)
	sw	s2, 16(sp)
	sw	s3, 12(sp)
	sw	s4, 8(sp)
	sw	s5, 4(sp)
	sw	s6, 0(sp)
	mv	s2, a1
	mv	s4, a0
	addi	s5, zero, -1
	call	strlen
	mv	s0, a0
	mv	a0, s2
	call	strlen
	mv	s3, a0
	add	a0, a0, s0
	addi	a0, a0, 1
	srai	a1, a0, 31
	call	malloc
	addi	s6, zero, 1
	mv	s1, a0
	blt	s0, s6, .LBB16_4
# %bb.1:
	and	a2, s0, s5
	mv	a0, s1
	mv	a1, s4
	call	memcpy
	blt	s3, s6, .LBB16_3
.LBB16_2:
	add	a0, s1, s0
	and	a2, s3, s5
	mv	a1, s2
	call	memcpy
	add	s0, s0, s3
.LBB16_3:
	add	a0, s1, s0
	sb	zero, 0(a0)
	mv	a0, s1
	lw	s6, 0(sp)
	lw	s5, 4(sp)
	lw	s4, 8(sp)
	lw	s3, 12(sp)
	lw	s2, 16(sp)
	lw	s1, 20(sp)
	lw	s0, 24(sp)
	lw	ra, 28(sp)
	addi	sp, sp, 32
	ret
.LBB16_4:
	mv	s0, zero
	bge	s3, s6, .LBB16_2
	j	.LBB16_3
.Lfunc_end16:
                                        # -- End function
	.globl	_string_parseInt        # -- Begin function _string_parseInt
	.p2align	2
_string_parseInt:                       # @_string_parseInt
# %bb.0:
	lbu	a2, 0(a0)
	addi	a1, a2, -48
	andi	a1, a1, 255
	addi	a3, zero, 9
	bltu	a3, a1, .LBB17_4
# %bb.1:
	mv	a5, zero
	mv	a3, zero
	mv	a1, zero
	addi	a6, zero, 10
.LBB17_2:                               # =>This Inner Loop Header: Depth=1
	mul	a7, a1, a6
	addi	a4, a5, 1
	sltu	a5, a4, a5
	slli	a1, a2, 24
	add	a2, a0, a4
	lbu	a2, 0(a2)
	add	a3, a3, a5
	srai	a1, a1, 24
	add	a1, a7, a1
	addi	a5, a2, -48
	andi	a7, a5, 255
	addi	a1, a1, -48
	mv	a5, a4
	bltu	a7, a6, .LBB17_2
# %bb.3:
	mv	a0, a1
	ret
.LBB17_4:
	mv	a0, zero
	ret
.Lfunc_end17:
	.size	_string_parseInt, .Lfunc_end17-_string_parseInt
                                        # -- End function
	.globl	_array_size             # -- Begin function _array_size
	.p2align	2
_array_size:                            # @_array_size
# %bb.0:
	lw	a0, -8(a0)
	ret
.Lfunc_end18:
                                        # -- End function
	.section	.rodata.str1.1,"aMS",@progbits,1
.L.str:
	.asciz	"%s"

.L.str.2:
	.asciz	"%d"

.L.str.3:
	.asciz	"%d\n"

	.ident	"clang version 6.0.0-1ubuntu2 (tags/RELEASE_600/final)"
	.section	".note.GNU-stack","",@progbits
