
; /\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/
; /\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/

	; ---------------------------------------------------------------------------------------------------------
	; ---------------------------------------------------------------------------------------------------------
	; This structure holds the information of the normal meshes, EMBM self-contained.
	Type FxMesh
		Field Mesh							; In this, the original mesh is modified, no other values needed.
	End Type

	; ---------------------------------------------------------------------------------------------------------
	; ---------------------------------------------------------------------------------------------------------
	; This structure holds the information of the normal meshes that are to be rendered after processing the
	; whole scene (for general distortions, mainly).
	Type FxMeshPostProcess
		Field Mesh							; Original FX Mesh
		Field NormalMesh					; Normal FX Mesh
		Field Position.tVector				; Position, Rotation and Scale 
		Field Rotation.tVector				; of the Normal FX Mesh, with respect
		Field Scale.tVector					; of the original mesh.
	End Type
	
; /\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/
; /\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/

	; ---- General system variables ----
	Global FxManager_Supported			= True
	Global FxManager_Activated			= True
	Global FxManager_LastState			= -1
	Global FxManager_Debug				= True
	Global FxManager_OnlyPowerOfTwo		= True
	Global FxManager_BorderFix			= True
	
	; ---- Overlay variables ----
	Global FxManager_Camera				= 0
	Global FxManager_Overlay			= 0
	Global FxManager_Overlay_Size		= 0
	Global FxManager_Overlay_RealSize	= 0
	Global FxManager_Overlay_Camera		= 0
	Global FxManager_Overlay_Colormap	= 0
	Global FxManager_Overlay_Normalmap	= 0
	Global FxManager_Overlay_Swap     	= 0

	; ---- Others ----
	Global FxManager_Texture_Disable  	= 0
	Global FxManager_Texture_Empty 		= 0

	; ---- Debug ----
	Global FxManager_Debug_Pass1     	= 0
	Global FxManager_Debug_Pass2   		= 0
	Global FxManager_Debug_Pass3   		= 0
	Global FxManager_Debug_Pass4   		= 0
	Global FxManager_Debug_Overall		= 0
	
; /\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/
; /\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/

	; Infinite value. When doing the second and third rendering pass, we need to offset all objects to infinite,
	; as we don't keep the Non-EMBM geometry data, wich may result in (yet again) rendering what shouldn't.
	Const FXMANAGER_INFINITE# = 9999.9

	; Since Blitz3D's ZBuffer precission is greatly poor, we need to scale up a little bit the meshes for the
	; goddamn renderer write a pixel when the EMBM geometry and EMBM normal-mapped geometry are rendered.
	Const FXMANAGER_DEPTHMINVALUE# = 0.01
	
; /\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/
; /\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/

	; ---- Startup and Shutdown -------------------------------------------------------------------------------
	; =========================================================================================================
	; =========================================================================================================
	Function FxManager_Startup()
		; Initializate Tom's DX7 DLL
		If (DX7_SetSystemProperties(SystemProperty("Direct3D7"), SystemProperty("Direct3DDevice7"), SystemProperty("DirectDraw7"), SystemProperty("AppHWND"), SystemProperty("AppHINSTANCE"))) Then
			RuntimeError("FxManager_Startup -> Unable to initialize DirectX 7.")
		End If
		If (DX7_SupportsBumpMapping() = False Or DX7_SupportsLumiBumpMapping() = False) Then
			FxManager_Supported	= False
		End If
		If (DX7_SupportsRenderTexture() = False) Then
			FxManager_Supported	= False
		End If

		If (FxManager_Activated = False) Then FxManager_Supported = False
		Return FxManager_Supported
	End Function

	; =========================================================================================================
	; =========================================================================================================
	Function FxManager_SetCamera(Camera, Bumpiness#)
		; Acquire camera
		FxManager_Camera = Camera

		If (FxManager_Supported = False) Then Return
		
		; Set EMBM information
		DX7_SetBumpInfo(Bumpiness#, -Bumpiness#, -Bumpiness#, Bumpiness#, 1, 0, 0)

		; Create textures
		FxManager_Texture_Disable	= CreateTexture(1, 1, 1) : FxManager_PaintTexture(FxManager_Texture_Disable, 155,120,20)
		FxManager_Texture_Empty		= CreateTexture(1, 1, 1) : FxManager_PaintTexture(FxManager_Texture_Empty, 0, 0, 0)
		
		; Compute the nearest texture size for the view
		If FxManager_OnlyPowerOfTwo Then
			FxManager_Overlay_Size  	 = FxManager_NearestPowerOfTwo(Max(GraphicsWidth(), GraphicsHeight()))
			FxManager_Overlay_RealSize 	 = FxManager_Overlay_Size
		Else
			FxManager_Overlay_Size  	 = Max(GraphicsWidth(), GraphicsHeight())
			FxManager_Overlay_RealSize 	 = FxManager_NearestPowerOfTwo(FxManager_Overlay_Size)
		End If

		; Compute aspect ratio and scale
		Aspect# = 1
		Scale#  = 1
		
		; Create overlay

			; Create the colormap
			DX7_UserRenderTexture()
			FxManager_Overlay_Colormap = CreateTexture(FxManager_Overlay_Size, FxManager_Overlay_Size, 1+16+32+256+512)
			DX7_AddZBuffer(FxManager_Overlay_Colormap)
			;ScaleTexture(FxManager_Overlay_Colormap, ScaleW#, ScaleH#)
			TextureBlend(FxManager_Overlay_Colormap, 3)

			DebugLog(TextureWidth(FxManager_Overlay_Colormap)+" vs "+Float(FxManager_Overlay_Size))
			DebugLog(TextureHeight(FxManager_Overlay_Colormap)+" vs "+Float(FxManager_Overlay_Size))
			
			; Create the normalmap
			DX7_UserRenderTexture()
			FxManager_Overlay_Normalmap = CreateTexture(FxManager_Overlay_Size, FxManager_Overlay_Size, 1+16+32+256+512)
			;ScaleTexture(FxManager_Overlay_Normalmap, ScaleW#, ScaleH#)
			DX7_AddZBuffer(FxManager_Overlay_Normalmap)

			; Create quad mesh and camera
			FxManager_Overlay_Camera   = CreateCamera() : CameraRange(FxManager_Overlay_Camera, .01, 1000) : CameraClsColor(FxManager_Overlay_Camera, 0, 0, 0)
			FxManager_Overlay		   = FxManager_OverlayCreate(1, 1, Scale#, Scale#*Aspect#, FxManager_Overlay_Camera) : EntityFX(FxManager_Overlay, 1)
			TranslateEntity(FxManager_Overlay_Camera, FXMANAGER_INFINITE#, FXMANAGER_INFINITE#, FXMANAGER_INFINITE#)
			
		; Texture the overlay
		EntityTexture(FxManager_Overlay, FxManager_Texture_Empty,      	0, 0)
		EntityTexture(FxManager_Overlay, FxManager_Overlay_Normalmap, 	0, 1)
		EntityTexture(FxManager_Overlay, FxManager_Overlay_Colormap, 	0, 2)

		; Everything's correct
		FxManager_Activated = True

	End Function

	; ---- Management methods ---------------------------------------------------------------------------------
	; =========================================================================================================
	; =========================================================================================================
	Function FxManager_RenderWorldFast(tween# = 1)
		; If the effects aren't supported, get away
		If (FxManager_Supported = False) Then : RenderWorld(tween#) : Return : End If
		
		; If not activated, get out
		If (FxManager_Activated = False Or FxManager_Supported = False) Then
			; If last state wasn't activated, hide all objects
			If (FxManager_LastState = False) Then
				RenderWorld(tween#)
				Return
			End If
			
			; Hide the overlay object and camera
			HideEntity(FxManager_Overlay)
			HideEntity(FxManager_Overlay_Camera)
			ShowEntity(FxManager_Camera)
			For fx.FxMesh = Each FxMesh
				ShowEntity(fx\Mesh)
			Next
			For fxpp.FxMeshPostProcess = Each FxMeshPostProcess
				ShowEntity(fxpp\Mesh)
				HideEntity(fxpp\NormalMesh)
			Next
			CameraClsMode(FxManager_Camera, True, True)
			CameraClsColor(FxManager_Camera, 0, 0, 0)
			CameraViewport(FxManager_Camera, 0, 0, GraphicsWidth(), GraphicsHeight())

			; Change last state to false and we're done
			FxManager_LastState = False

			FxManager_RenderingPassInterruption(1, 1)
			FxManager_RenderingPassInterruption(1, 2)
			FxManager_RenderingPassInterruption(1, 3)
			FxManager_RenderingPassInterruption(1, 4)
			
			RenderWorld(tween#)
			Return
		End If
		FxManager_LastState = FxManager_Activated

		; Prepare for render (Hide overlay and show real camera)
		CameraViewport(FxManager_Camera, 0, 0, FxManager_Overlay_Size, FxManager_Overlay_Size)
		HideEntity(FxManager_Overlay)
		HideEntity(FxManager_Overlay_Camera)
		ShowEntity(FxManager_Camera)
		
		; 1st Pass, render all geometry. Render it on the normalmap and then transfer to
		; colormap.

			; Hide all EMBM meshes effect meshes.
			For fx.FxMesh = Each FxMesh
				HideEntity(fx\Mesh)
			Next
			For fxpp.FxMeshPostProcess = Each FxMeshPostProcess
				ShowEntity(fxpp\Mesh)
				HideEntity(fxpp\NormalMesh)
			Next

			; Setup camera to erase both color and ZBuffer
			CameraClsMode(FxManager_Camera, True, True)
			CameraClsColor(FxManager_Camera, 0, 0, 0)
			
			; Render overlay map
			FxManager_RenderingPassInterruption(1, 1)
			DX7_SetRenderTarget(FxManager_Overlay_Normalmap)
				RenderWorld(tween#)
			DX7_RestoreRenderTarget()

		; 2nd pass, render all EMBM geometry

			; Offset camera to infinite
			TranslateEntity(FxManager_Camera, FXMANAGER_INFINITE#, FXMANAGER_INFINITE#, FXMANAGER_INFINITE#)
			
			; Show EMBM meshes, offsetted to infinite.
			For fx.FxMesh = Each FxMesh
				ShowEntity(fx\Mesh)
				TranslateEntity(fx\Mesh, FXMANAGER_INFINITE#, FXMANAGER_INFINITE#, FXMANAGER_INFINITE#)
			Next
			For fxpp.FxMeshPostProcess = Each FxMeshPostProcess
				HideEntity(fxpp\Mesh)
			Next
			
			; Setup camera to not erase anything
			CameraClsMode(FxManager_Camera, False, False)
			CameraClsColor(FxManager_Camera, 0, 0, 0)
			
			; Render overlay map
			FxManager_RenderingPassInterruption(1, 1)
			DX7_EnableRenderBumpTexture()
			DX7_SetRenderTarget(FxManager_Overlay_Normalmap)
				RenderWorld(tween#)
			DX7_RestoreRenderTarget()
			DX7_DisableRenderBumpTexture()
			
		; 3rd Pass, render post-process EMBM geometry to normal map
						
			; Show all EMBM normal meshes and obtain their positions, offsetted to infinite.
			For fx.FxMesh = Each FxMesh
				HideEntity(fx\Mesh)
				TranslateEntity(fx\Mesh, -FXMANAGER_INFINITE#, -FXMANAGER_INFINITE#, -FXMANAGER_INFINITE#)
			Next
			For fxpp.FxMeshPostProcess = Each FxMeshPostProcess
				HideEntity(fxpp\Mesh) : ShowEntity(fxpp\NormalMesh)

				PositionEntity(fxpp\NormalMesh, EntityX(fxpp\Mesh, True)+fxpp\Position\x#+FXMANAGER_INFINITE#, EntityY(fxpp\Mesh, True)+fxpp\Position\y#+FXMANAGER_INFINITE#, EntityZ(fxpp\Mesh, True)+fxpp\Position\z#+FXMANAGER_INFINITE#, True)
				RotateEntity(fxpp\NormalMesh,   EntityPitch(fxpp\Mesh, True)+fxpp\Rotation\x#, EntityYaw(fxpp\Mesh, True)+fxpp\Rotation\x#, EntityRoll(fxpp\Mesh, True)+fxpp\Rotation\x#, True)
				ScaleEntity(fxpp\NormalMesh,    EntityScaleX(fxpp\Mesh)+fxpp\Scale\x#+FXMANAGER_DEPTHMINVALUE#, EntityScaleY(fxpp\Mesh)+fxpp\Scale\y#+FXMANAGER_DEPTHMINVALUE#, EntityScaleZ(fxpp\Mesh)+fxpp\Scale\z#+FXMANAGER_DEPTHMINVALUE#, True)
			Next
				
			; Setup camera to erase only the color
			CameraClsMode(FxManager_Camera, True, False)
			CameraClsColor(FxManager_Camera, 127, 127, 255)

			; Transfer the contents of the normalmap (the colormap itself) to the real colormap texture
			DX7_CopySurface(0, 0, FxManager_Overlay_Size, FxManager_Overlay_Size, 0, 0, FxManager_Overlay_NormalMap, FxManager_Overlay_ColorMap)

			; == HACK == So render to texture doesn't like the first column of the texture? Copy second to first
			DX7_CopySurface(1, 0, 1, FxManager_Overlay_Size, 0, 0, FxManager_Overlay_ColorMap, FxManager_Overlay_ColorMap)

			; Border fix. When using non-power of two textures, not all the area of the buffer is filled. This
			; fix corrects this by copying texture areas
			If FxManager_OnlyPowerOfTwo=False And FxManager_BorderFix Then
				For i = 0 To (FxManager_Overlay_RealSize-FxManager_Overlay_Size)/2
					DX7_CopySurface(FxManager_Overlay_Size-1, 0, 1, FxManager_Overlay_Size,  FxManager_Overlay_Size+i, 0, FxManager_Overlay_ColorMap, FxManager_Overlay_ColorMap)
				Next
				For i = 0 To (FxManager_Overlay_RealSize-FxManager_Overlay_Size)/2
					DX7_CopySurface(0, FxManager_Overlay_Size-1, FxManager_Overlay_Size, 1,  0, FxManager_Overlay_Size+i, FxManager_Overlay_ColorMap, FxManager_Overlay_ColorMap)
				Next
			End If
	
			; Render normal map
			FxManager_RenderingPassInterruption(2, 1)
			DX7_SetRenderTarget(FxManager_Overlay_Normalmap)
				RenderWorld(tween#)
			DX7_RestoreRenderTarget()		

		; 3rd Pass, render the world using the overlay
			; Debug, take how much time this pass will take
			FxManager_Debug_Pass3 = MilliSecs()

			ShowEntity(FxManager_Overlay)
			ShowEntity(FxManager_Overlay_Camera)
			HideEntity(FxManager_Camera)
		
			; Hide all EMBM normal geometry
			For fxpp.FxMeshPostProcess = Each FxMeshPostProcess : HideEntity(fxpp\NormalMesh) : Next

			; Disable texture filtering
			;DX7_SetTextureStageState(8, 16, 1)
			;DX7_SetTextureStageState(8, 17, 1)
			;DX7_SetTextureStageState(8, 18, 1)

			; Call interruption
			FxManager_RenderingPassInterruption(3, 1)
			
			;Debug
			If (FxManager_Debug) Then
				Debug = KeyDown(42)
				If (Debug) Then
					FxManager_PaintTexture(FxManager_Texture_Empty, 255, 255, 255)
					EntityTexture(FxManager_Overlay, FxManager_Overlay_Normalmap, 	0, 0)
					EntityTexture(FxManager_Overlay, FxManager_Texture_Empty,   	0, 1)
					EntityTexture(FxManager_Overlay, FxManager_Texture_Empty,   	0, 2)
				Else
					FxManager_PaintTexture(FxManager_Texture_Empty, 0, 0, 0)
					EntityTexture(FxManager_Overlay, FxManager_Texture_Empty,   	0, 0)
					EntityTexture(FxManager_Overlay, FxManager_Overlay_Normalmap, 	0, 1)
					EntityTexture(FxManager_Overlay, FxManager_Overlay_Colormap, 	0, 2)
					DX7_EnableRenderBumpTexture()
				End If
			Else
				DX7_EnableRenderBumpTexture()
			EndIf

			; Render the overlay, at the infinite
			RenderWorld(tween#)
			DX7_DisableRenderBumpTexture()

			; Reenable texture filtering
			;DX7_SetTextureStageState(8, 16, 2)
			;DX7_SetTextureStageState(8, 17, 2)
			;DX7_SetTextureStageState(8, 18, 3)

			; Offset back the camera to normal space
			TranslateEntity(FxManager_Camera, -FXMANAGER_INFINITE#, -FXMANAGER_INFINITE#, -FXMANAGER_INFINITE#)

			; Debug, take how much time this pass will take
			FxManager_Debug_Pass3   = MilliSecs() - FxManager_Debug_Pass3
			FxManager_Debug_Overall = MilliSecs() - FxManager_Debug_Overall
			FxManager_RenderingPassInterruption(4, 1)
		; Done
	End Function
	
	; =========================================================================================================
	; =========================================================================================================
	Function FxManager_RegisterFxMesh.FxMesh(Mesh, Texture, NormalMap)
		; If effects aren't supported, leave entity as is
		If (FxManager_Supported = False) Then Return Null

		; Create FxMesh structure
		fx.FxMesh = New FxMesh
			; Setup structure
			fx\Mesh = Mesh

			; Setup mesh
			EntityFX(fx\Mesh, 1+16)

			; Apply bump map
			EntityTexture(Mesh, FxManager_Texture_Disable, 0, 0)
			EntityTexture(Mesh, NormalMap, 				   0, 1)
			EntityTexture(Mesh, Texture, 				   0, 2)
			
		; Done
		Return fx
	End Function

	; =========================================================================================================
	; =========================================================================================================
	Function FxManager_RegisterFxMeshPostProcess.FxMeshPostProcess(Mesh, NormalMap, Position.tVector, Rotation.tVector, Scale.tVector, Alpha#=1.0, NormalAlpha#=1.0, Parent=0)
		; If effects aren't supported, apply alpha values but leave as is
		If (FxManager_Supported = False) Then : EntityAlpha(Mesh, Alpha) : Return Null : End If
		DebugLog("Supported = "+FxManager_Supported)
		
		; Create FxMesh structure
		fxpp.FxMeshPostProcess = New FxMeshPostProcess
			; Setup structure
			fxpp\Mesh 		= Mesh
			fxpp\NormalMesh = CopyEntity(fxpp\Mesh, Parent)
			fxpp\Position	= Position
			fxpp\Rotation	= Rotation
			fxpp\Scale		= Scale

			; Setup meshes
			EntityFX(fxpp\Mesh, 8+16+32)	   : EntityAlpha(fxpp\Mesh, Alpha#)
			EntityFX(fxpp\NormalMesh, 1+16+32) : EntityAlpha(fxpp\NormalMesh, Alpha#)		
			
						
			; Apply bump maps
			Brush = CreateBrush(255, 255, 255)
			
				BrushTexture(Brush, NormalMap)
			
			PaintEntity(fxpp\NormalMesh, Brush)

		; Done
		Return fxpp
	End Function


	; =========================================================================================================
	; =========================================================================================================
	Function FxManager_RegisterFxMeshHierarchy(Entity, NormalMapTexture, EntityHidden=False, Alpha#=1.0, NormalAlpha#=1.0, Parent=0)
	End Function

	; =========================================================================================================
	; =========================================================================================================
	Function FxManager_RegisterFxMeshPostProcessHierarchy(Entity, NormalMapTexture, EntityHidden=False, Alpha#=1.0, NormalAlpha#=1.0, Parent=0)
	End Function
	
	; ---- Misc. methods --------------------------------------------------------------------------------------
	; =========================================================================================================
	; =========================================================================================================
	Function FxManager_PaintTexture(Texture, R, G, B)
		GfxBuffer = GraphicsBuffer()
		SetBuffer(TextureBuffer(Texture))
			Color(R, G, B) : Plot(0, 0)
		SetBuffer(GfxBuffer)
	End Function
	
	; =========================================================================================================
	; =========================================================================================================
	Function FxManager_NearestPowerOfTwo(Value)
		Nearest = 1
		While(Nearest < Value) : Nearest = Nearest Shl 1 : Wend
		Return Nearest
	End Function

	; =========================================================================================================
	; =========================================================================================================
	Function FxManager_OverlayCreate(Size#, Aspect#, U#=1.0, V#=1.0, Parent=0)		
		Aspect# = 1/Aspect#
		Quad    = CreateMesh(Parent)
		Surface = CreateSurface(Quad)
		AddVertex(Surface, -Size#, -Size#*Aspect#, 1, 0, V#)
		AddVertex(Surface,  Size#, -Size#*Aspect#, 1, U#, V#)
		AddVertex(Surface, -Size#,  Size#*Aspect#, 1, 0, 0)
		AddVertex(Surface,  Size#,  Size#*Aspect#, 1, U#, 0)
		AddTriangle(Surface, 0, 2, 1)
		AddTriangle(Surface, 2, 3, 1)
	
		Return Quad
	End Function

	; =========================================================================================================
	; =========================================================================================================
	Function FxManager_Max(a, b)
		If (a > b) Then Return a
		Return b
	End Function