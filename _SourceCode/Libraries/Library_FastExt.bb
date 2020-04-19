Const FE_VERSION$ = "1.17"

; New constants for texture creating (CreateTexture function only)
Const FE_ExSIZE = 2048
Const FE_RENDER = 4096
Const FE_ZRENDER = 8192

; New constants for brush Fx ( EntityFX or BrushFX functions only)
Const FE_WIRE = 64				; ?ису?тся только линии
Const FE_POINT = 128			; ?ису?тся только точки

; RenderPostprocess function constants
Const FE_NONE = 0
Const FE_DOF = 1
Const FE_Glow = 2
Const FE_Blur = 4
Const FE_Inverse = 8
Const FE_Grayscale = 16
Const FE_Contrast = 32
Const FE_BlurDirectional = 64
Const FE_BlurZoom = 128
Const FE_BlurSpin = 256
Const FE_BlurMotion = 512
Const FE_Overlay = 1024
Const FE_Posterize = 2048
Const FE_Rays = 4096


		; -------------------- DirectX7 constants (only for TextureBlendCustom function) -----------------------
		; Control
		Const D3DTOP_DISABLE  = 1   ; disables stage
		Const D3DTOP_SELECTARG1 = 2   ; the Default
		Const D3DTOP_SELECTARG2 = 3
		
		; Modulate
		Const D3DTOP_MODULATE  = 4   ; multiply args together
		Const D3DTOP_MODULATE2X = 5   ; multiply And 1 bit
		Const D3DTOP_MODULATE4X = 6   ; multiply And 2 bits
		
		; Add
		Const D3DTOP_ADD = 7  ; add arguments together
		Const D3DTOP_ADDSIGNED  = 8  ; add With -0.5 bias
		Const D3DTOP_ADDSIGNED2X = 9  ; As above but left 1 bit
		Const D3DTOP_SUBTRACT   = 10  ; Arg1 - Arg2, With no saturation
		Const D3DTOP_ADDSMOOTH  = 11  ; add 2 args, subtract product		Arg1 + Arg2 - Arg1*Arg2 = Arg1 + (1-Arg1)*Arg2
		
		; Linear alpha blend: Arg1*(Alpha) + Arg2*(1-Alpha)
		Const D3DTOP_BLENDDIFFUSEALPHA  = 12 ; iterated alpha
		Const D3DTOP_BLENDTEXTUREALPHA  = 13 ; texture alpha
		Const D3DTOP_BLENDFACTORALPHA   = 14 ; alpha from D3DRENDERSTATE_TEXTUREFACTOR		Linear alpha blend With pre-multiplied arg1 input: Arg1 + Arg2*(1-Alpha)
		Const D3DTOP_BLENDTEXTUREALPHAPM = 15 ; texture alpha
		Const D3DTOP_BLENDCURRENTALPHA  = 16 ; by alpha of current color
		
		; Specular mapping
		Const D3DTOP_PREMODULATE  = 17   ; modulate With Next texture before use
		Const D3DTOP_MODULATEALPHA_ADDCOLOR = 18   ; Arg1.RGB + Arg1.A*Arg2.RGB	 COLOROP only
		Const D3DTOP_MODULATECOLOR_ADDALPHA = 19   ; Arg1.RGB*Arg2.RGB + Arg1.A	COLOROP only
		Const D3DTOP_MODULATEINVALPHA_ADDCOLOR = 20 ; (1-Arg1.A)*Arg2.RGB + Arg1.RGB	COLOROP only
		Const D3DTOP_MODULATEINVCOLOR_ADDALPHA = 21 ; (1-Arg1.RGB)*Arg2.RGB + Arg1.A  COLOROP only
		
		; Bump mapping
		Const D3DTOP_BUMPENVMAP  = 22 ; per pixel env map perturbation
		Const D3DTOP_BUMPENVMAPLUMINANCE = 23 ; With luminance channel
		  ; This can do either diffuse Or specular bump mapping With correct input.
		  ; Performs the function (Arg1.R*Arg2.R + Arg1.G*Arg2.G + Arg1.B*Arg2.B)
		  ; where each component has been scaled And offset To make it signed.
		  ; The result is replicated into all four (including alpha) channels.
		  ; This is a valid COLOROP only.
		Const D3DTOP_DOTPRODUCT3 = 24
		
		Const FETOP_PROJECT   = $010000	; FastExt constant for 2D projective texturing TextureCoords=0 (UV0)
		Const FETOP_PROJECT0  = $010000	; FastExt constant for 2D projective texturing TextureCoords=0 (UV0)
		Const FETOP_PROJECT1 = $020000	; FastExt constant for 2D projective texturing TextureCoords=1 (UV1)

		Const FETOP_PROJECT3D  = $050000	; FastExt constant for 3D projective texturing TextureCoords=0 (UV0)
		Const FETOP_PROJECT3D0  = $050000	; FastExt constant for 3D projective texturing TextureCoords=0 (UV0)
		Const FETOP_PROJECT3D1 = $060000	; FastExt constant for 3D projective texturing TextureCoords=1 (UV1)



		; -------------------- DirectX7 constants (only for EntityBlendCustom & BrushBlendCustom functions) -----------------------
		Const D3DBLEND_ZERO   = 1
		Const D3DBLEND_ONE    = 2
		Const D3DBLEND_SRCCOLOR = 3
		Const D3DBLEND_INVSRCCOLOR    = 4
		Const D3DBLEND_SRCALPHA = 5
		Const D3DBLEND_INVSRCALPHA    = 6
		Const D3DBLEND_DESTALPHA = 7
		Const D3DBLEND_INVDESTALPHA   = 8
		Const D3DBLEND_DESTCOLOR = 9
		Const D3DBLEND_INVDESTCOLOR   = 10
		Const D3DBLEND_SRCALPHASAT    = 11
		Const D3DBLEND_BOTHSRCALPHA   = 12
		Const D3DBLEND_BOTHINVSRCALPHA  = 13


; New constants for texture stage blending (TextureBlend function)
Const FE_ALPHACURRENT = 	(D3DTOP_BLENDCURRENTALPHA Shl 8) Or D3DTOP_SELECTARG1
Const FE_ALPHAMODULATE = 	(D3DTOP_MODULATE Shl 8) Or D3DTOP_MODULATE
Const FE_BUMP = 	(D3DTOP_BUMPENVMAP Shl 8) Or D3DTOP_SELECTARG2
Const FE_BUMPLUM = 	(D3DTOP_BUMPENVMAPLUMINANCE Shl 8) Or D3DTOP_SELECTARG2
Const FE_PROJECT =	 FETOP_PROJECT Or (D3DTOP_MODULATE Shl 8) Or D3DTOP_MODULATE
Const FE_PROJECTSMOOTH = 	FETOP_PROJECT Or (D3DTOP_ADDSMOOTH Shl 8) Or D3DTOP_MODULATE
Const FE_MULTIPLY4X = 	(D3DTOP_MODULATE4X Shl 8)
Const FE_ADDSIGNED = 	(D3DTOP_ADDSIGNED Shl 8)
Const FE_ADDSIGNED2X = 	(D3DTOP_ADDSIGNED2X Shl 8)
Const FE_ADDSMOOTH =		(D3DTOP_ADDSMOOTH Shl 8)
Const FE_SUB =		(D3DTOP_SUBTRACT Shl 8)
Const FE_SPECULAR0 = (D3DTOP_MODULATEALPHA_ADDCOLOR Shl 8)
Const FE_SPECULAR1 = (D3DTOP_MODULATECOLOR_ADDALPHA Shl 8)
Const FE_SPECULAR2 = (D3DTOP_MODULATEINVALPHA_ADDCOLOR Shl 8)
Const FE_SPECULAR3 = (D3DTOP_MODULATEINVCOLOR_ADDALPHA Shl 8)


; New constants for brush (entity) blending (BrushBlend and EntityBlend function)
Const FE_INVALPHA = $010605
Const FE_INVCOLOR = $010406
Const FE_INVCOLORADD = $010402
Const FE_NOALPHA = $000101


; Global type for Gfx-driver capabilities
Type GfxDriverCapsEx_Type
	Field BrushBlendsSrc%
	Field BrushBlendsDest%
	Field TextureCaps%
	Field TextureBlends%
	Field TextureMaxStages%
	Field TextureMaxWidth%
	Field TextureMaxHeight%
	Field TextureMaxAspectRatio%
	Field ClipplanesMax%
	Field LightsMax%
	Field Bump%
	Field BumpLum%
	Field AnisotropyMax%
End Type
Global GfxDriverCapsEx.GfxDriverCapsEx_Type = New GfxDriverCapsEx_Type




Type Matrix3D
	Field m11#, m12#, m13#, m14#
	Field m21#, m22#, m23#, m24#
	Field m31#, m32#, m33#, m34#
	Field m41#, m42#, m43#, m44#
End Type 




; library system vars
Global FE_PivotSys% = 0
Global FE_InitExtFlag% = 0
Global FE_InitPostprocessFlag% = 0
Global FE_PostprocessTexture1% = 0
Global FE_PostprocessTexture2% = 0
Global FE_PostprocessTexture3% = 0
Global FE_PostprocessTexture4% = 0
Global FE_PostprocessTexture5% = 0



; Main function for library initialising, must call after command Graphics3D

Function InitExt% ()
	If FE_VERSION<>ExtVersion() Then
		RuntimeError "ERROR! FastExstension library - Incorrect versions for FastExt.dll (v"+ExtVersion()+") And FastExt.bb (v"+FE_VERSION+")"
	Else
		DebugLog "Init FastExtension library v"+FE_VERSION+"successful"
	EndIf
	If FE_InitExtFlag=0 Then
		FE_InitExtFlag = 1
		FE_PivotSys = CreatePivot()
		DeInitPostprocess()
		InitExt_ ( SystemProperty("Direct3DDevice7"), BackBuffer(), GfxDriverCapsEx )
	EndIf
End Function



; Function for render single entity or entity with childrens
Function RenderEntity% (entity%, camera%, clearViewport%=0, tween#=1.0)
	Return RenderEntity_ (entity, camera, tween, clearViewport, FE_PivotSys)
End Function



; Function for render group of entities (with childrens, if not hidden)
Function RenderGroup% (group%, camera%, clearViewport%=0, tween#=1.0)
	Return RenderGroup_ (group, camera, tween, clearViewport, FE_PivotSys)
End Function



Function TextureAnisotropy% (level%=0, index%=-1)
	Return TextureAnisotropy_ (level, index)
End Function



Function TextureLodBias% (bias#=-0.2, index%=-1)
	Return TextureLodBias_ (bias, index)
End Function



; Additional functions for ClipPlanes

Function CreateClipplane% (entity%=0,  x1#=0, y1#=0, z1#=0,  x2#=0, y2#=0, z2#=1,  x3#=1, y3#=0, z3#=0)
	If entity<>0 Then
		TFormPoint 0, 0, 0,entity,0  :  x1 = TFormedX()  :  y1 = TFormedY()  :  z1 = TFormedZ()
		TFormPoint 0, 0, 1,entity,0  :  x2 = TFormedX()  :  y2 = TFormedY()  :  z2 = TFormedZ()
		TFormPoint 1, 0, 0,entity,0  :  x3 = TFormedX()  :  y3 = TFormedY()  :  z3 = TFormedZ()
	EndIf
	Return CreateClipplane_ ( x1, y1, z1,  x2, y2, z2,  x3, y3, z3 )
End Function

Function AlignClipplane% (plane%,  entity%=0,  x1#=0, y1#=0, z1#=0,  x2#=0, y2#=0, z2#=1,  x3#=1, y3#=0, z3#=0)
	If entity<>0 Then
		TFormPoint 0, 0, 0,entity,0  :  x1 = TFormedX()  :  y1 = TFormedY()  :  z1 = TFormedZ()
		TFormPoint 0, 0, 1,entity,0  :  x2 = TFormedX()  :  y2 = TFormedY()  :  z2 = TFormedZ()
		TFormPoint 1, 0, 0,entity,0  :  x3 = TFormedX()  :  y3 = TFormedY()  :  z3 = TFormedZ()
	EndIf
	Return AlignClipplane_ ( plane,  x1, y1, z1,  x2, y2, z2,  x3, y3, z3 )
End Function



; Additional functions for Camera

Global MirrorCameraLast% = 0
Global MirrorCameraX# = 0
Global MirrorCameraY# = 0
Global MirrorCameraZ# = 0
Global MirrorCameraAX# = 0
Global MirrorCameraAY# = 0
Global MirrorCameraAZ# = 0
Global MirrorCameraParent% = 0

Function MirrorCamera% (camera%=0, entity%=0)
	If camera<>0 Then
		MirrorCameraLast = camera
		MirrorCameraX# = EntityX(camera,1)
		MirrorCameraY# = EntityY(camera,1)
		MirrorCameraZ# = EntityZ(camera,1)
		MirrorCameraAX# = EntityPitch(camera,1)
		MirrorCameraAY# = EntityYaw(camera,1)
		MirrorCameraAZ# = EntityRoll(camera,1)
		If entity<>0 Then
			MirrorCameraParent = ParentEntity(camera)
			EntityParent camera, entity, 1
			PositionEntity camera, EntityX(camera), -EntityY(camera), EntityZ(camera)
			RotateEntity camera, -EntityPitch(camera), EntityYaw(camera), -EntityRoll(camera)
			EntityParent camera,0,1
		Else
			PositionEntity camera, MirrorCameraX, -MirrorCameraY, MirrorCameraZ, 1
			RotateEntity camera, -MirrorCameraAX, MirrorCameraAY, -MirrorCameraAZ, 1
		EndIf
	EndIf
End Function

Function RestoreCamera% (camera%=0)
	If camera=0 Then camera=MirrorCameraLast
	If camera<>0 Then
		PositionEntity camera, MirrorCameraX, MirrorCameraY, MirrorCameraZ, 1
		RotateEntity camera, MirrorCameraAX, MirrorCameraAY, MirrorCameraAZ, 1
		If MirrorCameraParent<>0 Then EntityParent camera, MirrorCameraParent, 1
	EndIf	
End Function



Function SetBuffer% (buffer%)
	Return SetBuffer_ (buffer)
End Function

Function GetBuffer% ()
	Return SetBuffer_ (-1)
End Function

Function ClsColor% (red%, green%, blue%, alpha%=$FF, zValue#=1.0)
	Return ClsColor_ (red, green, blue, alpha, zValue)
End Function

Function Cls% (clearColor%=1, clearZBuffer%=1)
	Return Cls_ (clearColor, clearZBuffer)
End Function

Function WireFrame% (enable%=0)
	Return Wireframe_ (enable)
End Function

Function Bump% (enable%=-1)
	Return Bump_ (enable)
End Function

Function FreeTexture% (texture%)
	If texture<>0 Then
		Return FreeTexture_ (texture, TextureBuffer(texture))
	Else
		Return 0
	EndIf
End Function

Function ColorFilter% (red%=1, green%=1, blue%=1, alpha%=1)
	Return ColorFilter_ (red, green, blue, alpha)
End Function




Function TextureBlend% (texture%, blend%)
	TextureBlend_ texture, blend
End Function



; New function for custom texture blending (use D3DTOP_* constans only, see below)

Function TextureBlendCustom% (texture%, color_operation%, alpha_operation%=0, projection_flag%=0)
	If color_operation>24 Then color_operation=24
	If color_operation<1 Then color_operation=1
	If alpha_operation>24 Then alpha_operation=24
	If alpha_operation<0 Then alpha_operation=0
	projection_flag = projection_flag And $7
	TextureBlend texture, (projection_flag Shl 16) Or (color_operation Shl 8) Or alpha_operation
End Function



; New functions for custom entity (brush) blending (use D3DBLEND_* constans only, see below)

Function EntityBlendCustom% (entity%, source_blend%=1, destination_blend%=1, alphablending_enable%=0)
	If source_blend>13 Then source_blend=13
	If source_blend<1 Then source_blend=1
	If destination_blend>13 Then destination_blend=13
	If destination_blend<1 Then destination_blend=1
	If alphablending_enable<>0 Then alphablending_enable=1
	EntityBlend entity, (alphablending_enable Shl 16) Or (source_blend Shl 8) Or destination_blend
End Function

Function BrushBlendCustom% (brush%, source_blend%=1, destination_blend%=1, alphablending_enable%=0)
	If source_blend>13 Then source_blend=13
	If source_blend<1 Then source_blend=1
	If destination_blend>13 Then destination_blend=13
	If destination_blend<1 Then destination_blend=1
	If alphablending_enable<>0 Then alphablending_enable=1
	BrushBlend brush, (alphablending_enable Shl 16) Or (source_blend Shl 8) Or destination_blend
End Function





Function ExecAndExit% (file$="", command$="", workingDir$="")
	ExecAndExit_ file, command, workingDir
End Function




Function InitPostprocess% ()
	Local CurrentBuffer%, smallWidth%, smallHeight%
	If FE_InitPostprocessFlag=0
		smallWidth = GraphicsWidth()/3 : smallHeight = GraphicsHeight()/3
		FE_PostprocessTexture1 = CreateTexture ( GraphicsWidth(), GraphicsHeight(), 1 + 256 + FE_ExSIZE + FE_RENDER + FE_ZRENDER )
		FE_PostprocessTexture2 = CreateTexture ( smallWidth, smallHeight, 1 + 256 + FE_ExSIZE  + FE_RENDER )
		FE_PostprocessTexture3 = CreateTexture ( 16, 16, 1 )
		FE_PostprocessTexture4 = CreateTexture ( smallWidth, smallHeight, 1 + 256 + FE_ExSIZE  + FE_RENDER )
		FE_PostprocessTexture5 = CreateTexture ( GraphicsWidth(), GraphicsHeight(), 1 + 256 + FE_ExSIZE )		; comment this string if MotionBlur not needed (not used)

		CurrentBuffer = SetBuffer (TextureBuffer(FE_PostprocessTexture3))
		ClsColor 255,255,255 : Cls : SetBuffer BackBuffer()
		If InitPostprocess_ (BackBuffer(), TextureBuffer(FE_PostprocessTexture1), TextureBuffer(FE_PostprocessTexture2), TextureBuffer(FE_PostprocessTexture3), TextureBuffer(FE_PostprocessTexture4), TextureBuffer(FE_PostprocessTexture5))<>0 Then
			FE_InitPostprocessFlag = 1
		Else
			If FE_PostprocessTexture1<>0 Then FreeTexture FE_PostprocessTexture1
			If FE_PostprocessTexture2<>0 Then FreeTexture FE_PostprocessTexture2
			If FE_PostprocessTexture3<>0 Then FreeTexture FE_PostprocessTexture3
			If FE_PostprocessTexture4<>0 Then FreeTexture FE_PostprocessTexture4
			If FE_PostprocessTexture5<>0 Then FreeTexture FE_PostprocessTexture5
		EndIf
		SetBuffer CurrentBuffer
	EndIf
	Return FE_InitPostprocessFlag
End Function

Function DeInitPostprocess% ()
	If FE_InitPostprocessFlag<>0 Then
		If FE_PostprocessTexture1<>0 Then FreeTexture FE_PostprocessTexture1
		If FE_PostprocessTexture2<>0 Then FreeTexture FE_PostprocessTexture2
		If FE_PostprocessTexture3<>0 Then FreeTexture FE_PostprocessTexture3
		If FE_PostprocessTexture4<>0 Then FreeTexture FE_PostprocessTexture4
		If FE_PostprocessTexture5<>0 Then FreeTexture FE_PostprocessTexture5
		FE_InitPostprocessFlag = 0
	EndIf
End Function

Function RenderPostprocess% (flags%=0, x%=0, y%=0, width%=0, height%=0)
	If InitPostprocess()<>0 Then RenderPostprocess_ flags, x, y, width, height
End Function

Function CustomPostprocessDOF% (near#=10.0, far#=100.0, direction%=1, level%=3, blurRadius#=0.35, quality%=1)
	CustomPostprocessDOF_ near, far, direction, level, blurRadius, quality
End Function

Function CustomPostprocessGlow% (alpha#=1.0, darkPasses%=2, blurPasses%=4, blurRadius#=0.35, quality%=1, red%=255, green%=255, blue%=255, alphaTexture%=0)
	CustomPostprocessGlow_ alpha, darkPasses, blurPasses, blurRadius, quality, red, green, blue, alphaTexture
End Function

Function CustomPostprocessBlur% (alpha#=1.0, blurPasses%=4, blurRadius#=0.35, quality%=1, red%=255, green%=255, blue%=255, alphaTexture%=0)
	CustomPostprocessBlur_ alpha, blurPasses, blurRadius, quality, red, green, blue, alphaTexture
End Function

Function CustomPostprocessInverse% (alpha#=1.0, red%=255, green%=255, blue%=255, alphaTexture%=0)
	CustomPostprocessInverse_ alpha, red, green, blue, alphaTexture
End Function

Function CustomPostprocessGrayscale% (alpha#=1.0, brightness#=1.0, inverse%=0, alphaTexture%=0)
	CustomPostprocessGrayscale_ alpha, brightness, inverse, alphaTexture
End Function

Function CustomPostprocessContrast% (alpha#=4.0, method%=0, red%=255, green%=255, blue%=255, alphaTexture%=0)
	CustomPostprocessContrast_ alpha#, method, red, green, blue, alphaTexture
End Function

Function CustomPostprocessBlurDirectional% (angle#=0, alpha#=1, blurPasses%=4, blurRadius#=0.35, quality%=1, red%=255, green%=255, blue%=255, alphaTexture%=0)
	CustomPostprocessBlurDirectional_ angle, alpha, blurPasses, blurRadius, quality, red, green, blue, alphaTexture
End Function

Function CustomPostprocessBlurZoom% (x#=0.5, y#=0.5, zoomFactor#=105, alpha#=1, blurPasses%=4, quality%=1, red%=255, green%=255, blue%=255, alphaTexture%=0)
	CustomPostprocessBlurZoom_ x, y, zoomFactor, alpha, blurPasses, quality, red, green, blue, alphaTexture
End Function

Function CustomPostprocessBlurSpin% (x#=0.5, y#=0.5, spinAngle#=4, alpha#=1, blurPasses%=4, quality%=1, red%=255, green%=255, blue%=255, alphaTexture%=0)
	CustomPostprocessBlurSpin_ x, y, spinAngle, alpha, blurPasses, quality, red, green, blue, alphaTexture
End Function

Function CustomPostprocessBlurMotion% (alpha#=0.9, originX#=0, originY#=0, handleX#=0.5, handleY#=0.5, scaleX#=100, scaleY#=100, angle#=0, blend%=0, red%=255, green%=255, blue%=255, alphaTexture%=0)
; defaults were Function CustomPostprocessBlurMotion% (alpha#=0.9, originX#=0, originY#=0, handleX#=0.5, handleY#=0.5, scaleX#=100, scaleY#=100, angle#=0, blend%=0, red%=255, green%=255, blue%=255, alphaTexture%=0)
	CustomPostprocessBlurMotion_ alpha, originX, originY, handleX, handleY, scaleX, scaleY, angle, blend, red, green, blue, alphaTexture
End Function

Function CustomPostprocessOverlay% (alpha#=0.5, blend%=0, red%=255, green%=255, blue%=255, alphaTexture%=0)
	CustomPostprocessOverlay_ alpha, blend, red, green, blue, alphaTexture
End Function

Function CustomPostprocessRays% (centerX#=0.5, centerY#=0.5, zoomFactor#=105, alpha#=1, darkPasses%=2, blurPasses%=4, quality%=1, red%=255, green%=255, blue%=255, alphaTexture%=0)
	CustomPostprocessRays_% (centerX, centerY, zoomFactor, alpha, darkPasses, blurPasses, quality, red, green, blue, alphaTexture)
End Function


Function DeInitExt% ()
	If FE_InitExtFlag<>0 Then
		FE_InitExtFlag = 0
		SetBuffer BackBuffer()
		FreeEntity FE_PivotSys
		DeInitPostprocess
		DeInitExt_
	EndIf
End Function


;-------------------------------------------------------
;-------------------------------------------------------
;-------------------------------------------------------

Type tSunRays
	Field X#, Y#, Z#
	Field Pitch#, Yaw#, Roll#
	Field Alpha#
	Field FX
End Type 

Global sunrays.tSunRays = New tSunRays

Function Postprocess_BlurHandle(enable=0, blurmotion=0, blurdirect=0, blurzoom=0, bluramount#=0, blurdirection#=0)
		If bluramount# > 0.617 Then bluramount# = 0.617
		MBlurAmount# = bluramount#
		BlurCenterX = Float(MouseX())/Float(GraphicsWidth())
		BlurCenterY = Float( MouseY())/Float(GraphicsHeight())
		CustomPostprocessBlurZoom(BlurCenterX,BlurCenterY, 125, MBlurAmount#, 4, 1, 255, 255, 255, PostProcess_AlphaTexture)	
		CustomPostprocessBlurMotion MBlurAmount#,  0, 0, 0.5,  0.5,  100.0,  100.0,  0,     0,  255,  255,  255
		CustomPostprocessBlurDirectional(blurdirection#, MBlurAmount#+0.1, 4, 0.51, 1, 255,255,255, PostProcess_AlphaTexture)
		If blurzoom>0 Then RenderPostprocess(FE_BLURZOOM)
		If blurmotion>0 Then RenderPostprocess(FE_BLURMOTION) 
		If blurdirect>0 Then RenderPostprocess(FE_BLURDIRECTIONAL)
End Function

Function Postprocess_SunRays(enable=0)
	If enable<>0 Then 
		For c.tCamera = Each tCamera
			CameraProject c\Entity, EntityX(Game\Stage\Properties\Sun,1), EntityY(Game\Stage\Properties\Sun,1), EntityZ(Game\Stage\Properties\Sun,1)
				If ProjectedZ()>0 Then
				TFormVector 0, 0, 1, c\Entity, Game\Stage\Properties\Sun
				sunrays\pitch# =  Abs(Sin(VectorPitch (TFormedX(), TFormedY(), TFormedZ())))   :   sunrays\pitch# = sunrays\pitch#*sunrays\pitch#
				sunrays\yaw# = Abs(Sin(VectorYaw (TFormedX(), TFormedY(), TFormedZ())))   :   sunrays\yaw# = sunrays\yaw#*sunrays\yaw#
				sunrays\alpha# = (1.0 - sunrays\pitch#) * (1.0 - sunrays\yaw#)
				sunrays\x# = ProjectedX()/Float(GraphicsWidth())
				sunrays\y# = ProjectedY()/Float(GraphicsHeight())
				CustomPostprocessRays sunrays\x#, sunrays\y#, 115, 0.8 * sunrays\alpha#-0.15, 4, 4, 3, 180, 240, 255		; <<< Customize Rays postprocess FX
				RenderPostprocess(FE_RAYS)
			Else
				RenderPostprocess(FE_NONE)
			EndIf
		Next
	EndIf
End Function