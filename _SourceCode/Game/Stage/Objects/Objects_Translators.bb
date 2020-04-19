Function Object_LockCamera(o.tObject,p.tPlayer)

	;control lock
	Select o\Translator\LockControl#
		Case 0:
			Select o\ObjType
				Case OBJTYPE_LOCKER,OBJTYPE_NODE,OBJTYPE_NODE2,OBJTYPE_FAN,OBJTYPE_BFAN,OBJTYPE_BFANLOW,OBJTYPE_FORCER,OBJTYPE_ACCEL:
					Game\ControlLock=0
				Default: Game\ControlLock=0.2*secs#
			End Select
		Case 1: Game\ControlLock=1800*secs#
		Case 2: Game\ControlLock=1*secs#
		Case 3: Game\ControlLock=3*secs#
		Case 4: Game\ControlLock=0.5*secs#
		Case 5: Game\ControlLock=2*secs#
	End Select
	If o\Translator\LockControl#>0 Then Input_ResetActionInput2()

	;cam lock
	If o\Translator\LockCam#=0 Then
		Game\CamLock=0
	Else
		Game\CamLock2=0
		Select o\Translator\LockCam#
			Case 1: Game\CamLock=1800*secs#
			Case 2: Game\CamLock=2*secs#
			Case 3: Game\CamLock=5*secs#
			Case 4: Game\CamLock=1*secs#
			Case 5: Game\CamLock=3.5*secs#
		End Select
		cam\Lock\PreviousPos=cam\Lock\Pos
		If o\Translator\CamPos>=10 Then
			cam\Lock\Pos=o\Translator\CamPos-10
			cam\Lock\Immediate=1
		Else
			cam\Lock\Pos=o\Translator\CamPos
			cam\Lock\Immediate=0
		EndIf
		If cam\Lock\Pos>0 Then
			cam\Lock\Position\x#=o\Translator\CamPosition\x# : cam\Lock\Position\y#=o\Translator\CamPosition\y# : cam\Lock\Position\z#=o\Translator\CamPosition\z#
			If Not(cam\Lock\PosTimer>0) Then PositionEntity cam\Lock\PosMesh, p\Objects\Position\x#, p\Objects\Position\y#, p\Objects\Position\z#, 1
			PositionEntity cam\Lock\PosMeshTarget, cam\Lock\Position\x#, cam\Lock\Position\y#, cam\Lock\Position\z#, 1
		EndIf
		cam\Lock\Rotation\x#=o\Translator\CamRotation\x# : cam\Lock\Rotation\y#=o\Translator\CamRotation\y# : cam\Lock\Rotation\z#=o\Translator\CamRotation\z#
		If o\Translator\CamZoom#<>0 Then cam\Lock\Zoom#=o\Translator\CamZoom# Else cam\Lock\Zoom#=21
		If o\Translator\CamSpeed#<>0 Then cam\Lock\Speed#=o\Translator\CamSpeed#/10.0 Else cam\Lock\Speed#=30/10.0
	EndIf

	;run lock
	Select o\Translator\LockRun#
		Case 0: Game\RunLock=0
		Case 1: Game\RunLock=1800*secs#
		Case 2: Game\RunLock=0.5*secs#
		Case 3: Game\RunLock=1.5*secs#
	End Select
	If o\Translator\LockRun#>0 Then
		If o\Power#>4.5 Then p\RunLockSpeed#=o\Power# Else p\RunLockSpeed#=4.5
	EndIf

End Function

Function Object_LockDestination(o.tObject,p.tPlayer)

	If o\Translator\HasDestination=1 Then
		p\GoDestination=True
		p\DestinationX#=o\Translator\Destination\x#
		p\DestinationY#=o\Translator\Destination\y#
		p\DestinationZ#=o\Translator\Destination\z#
		p\DestinationSpeed#=o\Power#*1.5
		
		PositionEntity p\Objects\DestinationTarget, p\DestinationX#, p\DestinationY#, p\DestinationZ#, 1
		p\DestinationSaverPreviousDistance#=EntityDistance(p\Objects\Entity,p\Objects\DestinationTarget)
	Else
		p\GoDestination=False
	EndIf
	p\DestinationSaverTimer=0

End Function

;-------------------------------------------------------------------------------------------------------------------------------

Function Object_CreateQuad(o.tObject)
	o\IValues[0] = CopyEntity(MESHES(Mesh_Quad), Game\Stage\Root)
	EntityBlend o\IValues[0], 3
	EntityFX o\IValues[0], 1+16
	EntityTexture o\IValues[0], Object_Texture_Quad
	EntityAlpha o\IValues[0], 0.0
	PositionEntity o\IValues[0], o\InitialPosition\x#, o\InitialPosition\y#, o\InitialPosition\z#
	RotateEntity o\IValues[0], o\InitialRotation\x#, o\InitialRotation\y#, o\InitialRotation\z#
	MoveEntity o\IValues[0], 0, 1, 0
	Select o\ObjType
		Case OBJTYPE_HOOP,OBJTYPE_HOOP:
			ScaleEntity o\IValues[0], 2*1.25, 2*1.25, 2*1.25
		Case OBJTYPE_RAMP:
			ScaleEntity o\IValues[0], 3*1.25, 3*1.25, 3*1.25
		Case OBJTYPE_ACCEL:
			ScaleEntity o\IValues[0], 5*1.25, 5*1.25, 5*1.25
		Default:
			ScaleEntity o\IValues[0], 1*1.25, 1*1.25, 1*1.25
	End Select
	o\FValues[0] = 0
End Function

Function Object_QuadEffect(o.tObject,p.tPlayer,d.tDeltaTime)
	Select o\ObjType
		Case OBJTYPE_LOCKER,OBJTYPE_FORCER,OBJTYPE_NODE,OBJTYPE_NODE2,OBJTYPE_FAN,OBJTYPE_BFAN,OBJTYPE_BFANLOW:
			Return
	End Select

	If o\State = 1 Then
		ShowEntity(o\IValues[0])
		o\FValues[0] = o\FValues[0] + 0.05*d\Delta
		ScaleEntity o\IValues[0], (1+o\FValues[0]*6)*1.25, (3+o\FValues[0]*6)*1.25, (1+o\FValues[0]*6)*1.25
		TurnEntity o\IValues[0], 0, 5*d\Delta, 0
		EntityAlpha o\IValues[0], (1-o\FValues[0])*0.7

		Select o\ObjType
			Case OBJTYPE_HOOP,OBJTYPE_THOOP:
				ScaleEntity o\Entity, 1.5 + Sin (o\FValues[0]*180)*1.5, 1.5 + Sin (o\FValues[0]*180)*1.5, 1.5 + Sin (o\FValues[0]*180)*1.5
			Default:
				ScaleEntity o\Entity, 1.5, 1.5 + Sin (o\FValues[0]*180)*1.5, 1.5
				If o\EntityX<>0 Then ScaleEntity o\EntityX, 1.5, 1.5 + Sin (o\FValues[0]*180)*1.5, 1.5
		End Select

		If o\FValues[0] >= 1.0 Then
			ScaleEntity o\IValues[0], 1*1.25, 1*1.25, 1*1.25
			o\FValues[0] = 0
			o\State = 0
			ScaleEntity o\Entity, 1.5, 1.5, 1.5
			If o\EntityX<>0 Then ScaleEntity o\EntityX, 1.5, 1.5, 1.5
		EndIf
	Else
		HideEntity(o\IValues[0])
	EndIf
End Function

; /\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/
; /\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/

	Function Object_Translator_Create.tObject(x#, y#, z#, pitch#, yaw#, roll#, power#, special#=0)
		o.tObject = New tObject : o\ObjType = TempAttribute\ObjectNo : o\ID=TempAttribute\ObjectID
		Select o\ObjType
			Case OBJTYPE_SPRING,OBJTYPE_BSPRING,OBJTYPE_SPRINGX,OBJTYPE_SPRINGTRAP,OBJTYPE_SPRINGTRAPX:
				o\CanHoming=True
			Default:
				o\CanHoming=False
		End Select
		o\ThisIsATranslator=True : o\Translator = New tObject_Translator : o\HasValuesetTranslator=True

		Select o\ObjType
			Case OBJTYPE_SPRING,OBJTYPE_SPRINGX,OBJTYPE_SPRINGTRAP,OBJTYPE_SPRINGTRAPX: Object_CreateHitBox(HITBOXTYPE_SPEEDY,o,6.5,6.5,6.5)
			Case OBJTYPE_BSPRING: Object_CreateHitBox(HITBOXTYPE_SPEEDY_BSPRING,o,6.5,6.5,6.5)
			Case OBJTYPE_PAD: Object_CreateHitBox(HITBOXTYPE_SPEEDY,o,8.5,6,8.5)
			Case OBJTYPE_RAMP: Object_CreateHitBox(HITBOXTYPE_SPEEDY,o,10,6,10)
			Case OBJTYPE_HOOP,OBJTYPE_THOOP: Object_CreateHitBox(HITBOXTYPE_SPEEDY,o,7.5,5.5,7.5)
			Case OBJTYPE_ACCEL: Object_CreateHitBox(HITBOXTYPE_SPEEDY,o,17,17,17)
			Case OBJTYPE_LOCKER: Object_CreateHitBox(HITBOXTYPE_SPEEDY_LOCKER,o,15,15,15)
			Case OBJTYPE_FORCER: Object_CreateHitBox(HITBOXTYPE_NORMAL_LOCKER,o,8.5,6,8.5)
			Case OBJTYPE_NODE,OBJTYPE_NODE2: Object_CreateHitBox(HITBOXTYPE_NORMAL,o,8.5,6,8.5)
			Case OBJTYPE_FAN: Object_CreateHitBox(HITBOXTYPE_FAN,o,13.5,70,13.5)
			Case OBJTYPE_BFAN: Object_CreateHitBox(HITBOXTYPE_FAN,o,50.25,470,50.25)
			Case OBJTYPE_BFANLOW: Object_CreateHitBox(HITBOXTYPE_FAN,o,50.25,186,50.25)
		End Select

		Object_Acquire_Position(o,x#,y#,z#)
		Select o\ObjType
			Case OBJTYPE_BSPRING: Object_Acquire_Rotation(o,0,yaw#,0)
			Case OBJTYPE_LOCKER: Object_Acquire_Rotation(o,90,yaw#,0)
			Case OBJTYPE_FAN,OBJTYPE_BFAN,OBJTYPE_BFANLOW: Object_Acquire_Rotation(o,0,Rand(1,360),0)
			Default: Object_Acquire_Rotation(o,pitch#,yaw#,roll#)
		End Select
		Object_Acquire_Power(o,power#)
		Object_Acquire_Lock(o,TempAttribute\lockcontrol#,TempAttribute\lockcam#,TempAttribute\lockrun#)
		Object_Acquire_Camera(o,TempAttribute\campos#,TempAttribute\camx#,TempAttribute\camy#,TempAttribute\camz#,TempAttribute\campitch#,TempAttribute\camyaw#,TempAttribute\camroll#,TempAttribute\camzoom#,TempAttribute\camspeed#)
		Select o\ObjType
			Case OBJTYPE_FAN,OBJTYPE_BFAN,OBJTYPE_BFANLOW,OBJTYPE_LOCKER: Object_Acquire_Destination(o,0,TempAttribute\dx#,TempAttribute\dy#,TempAttribute\dz#)
			Default: Object_Acquire_Destination(o,TempAttribute\hasd#,TempAttribute\dx#,TempAttribute\dy#,TempAttribute\dz#)
		End Select

		o\State=0

		Select o\ObjType
			Case OBJTYPE_LOCKER,OBJTYPE_FORCER:
				o\Translator\LockerBType=special#
			Case OBJTYPE_FAN,OBJTYPE_BFAN,OBJTYPE_BFANLOW:
				o\Translator\FanInvisible=special#
		End Select

		Select o\ObjType
			Case OBJTYPE_SPRING:
				o\Entity = CopyEntity(MESHES(SmartEntity(Mesh_Spring)), Game\Stage\Root)
			Case OBJTYPE_BSPRING:
				o\Entity = CopyEntity(MESHES(SmartEntity(Mesh_BSpring)), Game\Stage\Root)
			Case OBJTYPE_SPRINGX:
				o\Entity = CopyEntity(MESHES(SmartEntity(Mesh_SpringX)), Game\Stage\Root)
				Animate o\Entity,1,0.15,1
			Case OBJTYPE_SPRINGTRAP:
				o\Entity = CopyEntity(MESHES(SmartEntity(Mesh_SpringTrap)), Game\Stage\Root)
			Case OBJTYPE_SPRINGTRAPX:
				o\Entity = CopyEntity(MESHES(SmartEntity(Mesh_SpringTrapX)), Game\Stage\Root)
				Animate o\Entity,1,0.15,1
			Case OBJTYPE_PAD: o\Entity = CopyEntity(MESHES(SmartEntity(Mesh_DashPanel)), Game\Stage\Root)
			Case OBJTYPE_RAMP: o\Entity = CopyEntity(MESHES(SmartEntity(Mesh_DashRamp)), Game\Stage\Root)
			Case OBJTYPE_HOOP: o\Entity = CopyEntity(MESHES(SmartEntity(Mesh_DashHoop)), Game\Stage\Root)
			Case OBJTYPE_THOOP: o\Entity = CopyEntity(MESHES(SmartEntity(Mesh_RainbowHoop)), Game\Stage\Root)
			Case OBJTYPE_ACCEL: o\Entity = CopyEntity(MESHES(SmartEntity(Mesh_Accelerator)), Game\Stage\Root)
			Case OBJTYPE_LOCKER:
				If (Menu\Settings\Debug#=1 and Menu\Settings\DebugNodes#=1) Then
					o\Entity = CopyEntity(MESHES(SmartEntity(Mesh_Locker)), Game\Stage\Root)
				Else
					o\Entity = CreatePivot()
				EndIf
			Case OBJTYPE_FORCER,OBJTYPE_NODE,OBJTYPE_NODE2:
				If (Menu\Settings\Debug#=1 and Menu\Settings\DebugNodes#=1) Then
					o\Entity = CopyEntity(MESHES(SmartEntity(Mesh_Locker2)), Game\Stage\Root)
				Else
					o\Entity = CreatePivot()
				EndIf
			Case OBJTYPE_FAN:
				If o\Translator\FanInvisible=0 Or (Menu\Settings\Debug#=1 and Menu\Settings\DebugNodes#=1) Then
					o\Entity = CopyEntity(MESHES(SmartEntity(Mesh_Fan)), Game\Stage\Root)
					Animate(o\Entity,1,0.3,1)
					If Not(o\Translator\FanInvisible=0) Then
						EntityColor(o\Entity,0,0,0)
					Else
						EntityType(o\Entity,COLLISION_OBJECT)
					EndIf
				Else
					o\Entity = CreatePivot()
				EndIf
			Case OBJTYPE_BFAN,OBJTYPE_BFANLOW:
				If o\Translator\FanInvisible=0 Or (Menu\Settings\Debug#=1 and Menu\Settings\DebugNodes#=1) Then
					o\Entity = CopyEntity(MESHES(SmartEntity(Mesh_Fan)), Game\Stage\Root)
					ScaleEntity(o\Entity, 5.5, 5.5, 5.5)
					Animate(o\Entity,1,0.3,1)
					If Not(o\Translator\FanInvisible=0) Then
						EntityColor(o\Entity,0,0,0)
					Else
						EntityType(o\Entity,COLLISION_OBJECT)
					EndIf
				Else
					o\Entity = CreatePivot()
					ScaleEntity(o\Entity, 5.5, 5.5, 5.5)
				EndIf
		End Select

		If o\Objtype = OBJTYPE_THOOP Then o\Translator\THoopPointDisabler = 0

		Select o\ObjType
			Case OBJTYPE_PAD: o\EntityX = CopyEntity(MESHES(SmartEntity(Mesh_DashPanelPads)), Game\Stage\Root)
			Case OBJTYPE_RAMP: o\EntityX = CopyEntity(MESHES(SmartEntity(Mesh_DashRampPads)), Game\Stage\Root)
			Case OBJTYPE_ACCEL: o\EntityX = CopyEntity(MESHES(SmartEntity(Mesh_AcceleratorLight)), Game\Stage\Root)
		End Select

		Select o\ObjType
			Case OBJTYPE_LOCKER,OBJTYPE_FORCER,OBJTYPE_NODE,OBJTYPE_NODE2,OBJTYPE_FAN,OBJTYPE_BFAN,OBJTYPE_BFANLOW:
			Default: Object_CreateQuad(o)
		End Select

		Return o
	End Function
	
	; =========================================================================================================
	
	Function Object_Translator_Update(o.tObject, p.tPlayer, d.tDeltaTime)

		; Movement
		If o\ObjType=OBJTYPE_SPRINGX and o\InitialRotation\x#=0 Then
			If o\State=1 Then
				If o\Position\y#<o\InitialPosition\y#+100 Then MoveEntity o\Entity, 0,0.5*d\Delta,0
			ElseIf o\State=0
				If o\Position\y#>o\InitialPosition\y# Then MoveEntity o\Entity, 0,-0.1*d\Delta,0
			EndIf
		EndIf

		; For fan
		If (o\ObjType=OBJTYPE_FAN Or o\ObjType=OBJTYPE_BFAN Or o\ObjType=OBJTYPE_BFANLOW) Then
			If o\Translator\FanInvisible=0 And ChannelPlaying(o\Translator\Channel_Fan)=False Then o\Translator\Channel_Fan=EmitSmartSound(Sound_Fan,o\Entity)
			Select o\ObjType
				Case OBJTYPE_BFAN: ParticleTemplate_Call(o\Particle, PARTICLE_OBJECT_WINDTUNNEL, o\Entity, 4.5, 0, 60, 0, 0, 0.085)
				Case OBJTYPE_BFANLOW: ParticleTemplate_Call(o\Particle, PARTICLE_OBJECT_WINDTUNNEL, o\Entity, 4.5, 0, 6, 0, 0, 0.085)
			End Select
			If (Abs(p\Objects\Position\x# - o\Position\x#) < o\HitBox\x#) And (p\Objects\Position\y# > o\Position\y#-5) And (p\Objects\Position\y# < o\Position\y#+o\HitBox\y#+30) And (Abs(p\Objects\Position\z# - o\Position\z#) < o\HitBox\z#) Then p\FloatTimer=1.05*secs#
		EndIf

		; Update timer
		If o\Translator\WasJustUsedTimer>0 Then o\Translator\WasJustUsedTimer=o\Translator\WasJustUsedTimer-timervalue#

		; Player collided with object
		If (Not(o\Translator\WasJustUsedTimer>0)) And o\Hit And p\TranslatorsTouched<3 Then
			Select o\ObjType
				Case OBJTYPE_LOCKER,OBJTYPE_FORCER,OBJTYPE_NODE,OBJTYPE_NODE2,OBJTYPE_FAN,OBJTYPE_BFAN,OBJTYPE_BFANLOW:
				Default: o\State = 1
			End Select
			o\FValues[0] = 0.0

			If (Not(p\Action=ACTION_HURT Or p\Action=ACTION_DIE)) and (Not(o\ObjType=OBJTYPE_LOCKER)) Then
				; Transform the Speed vector to the Player space
				Select o\ObjType
					Case OBJTYPE_BFAN,OBJTYPE_BFANLOW: ScaleEntity o\Entity, 5.5, 5.5, 5.5
					Default: ScaleEntity o\Entity, 1.5, 1.5, 1.5
				End Select

				; Make Sonic a move
				Select o\ObjType
					Case OBJTYPE_SPRING,OBJTYPE_SPRINGX,OBJTYPE_BSPRING,OBJTYPE_SPRINGTRAP,OBJTYPE_SPRINGTRAPX,OBJTYPE_RAMP,OBJTYPE_HOOP,OBJTYPE_THOOP,OBJTYPE_ACCEL,OBJTYPE_FAN,OBJTYPE_BFAN,OBJTYPE_BFANLOW:
						p\Motion\Ground = False
						Player_Align(p)
					Case OBJTYPE_PAD,OBJTYPE_FORCER:
						If p\Motion\Ground = False Then Player_Align(p)
						p\Motion\Ground = True
				End Select
				TFormVector 0, 1, 0, o\Entity, p\Objects\Entity
				If o\Power#>0 Then
					Select o\ObjType
						Case OBJTYPE_NODE:
						Case OBJTYPE_FAN,OBJTYPE_BFAN,OBJTYPE_BFANLOW:
							If p\Objects\Position\y# < o\Position\y#+o\HitBox\y# Then
								Select o\ObjType
									Case OBJTYPE_BFAN: p\Motion\Speed\y#=(-p\Physics\FLOATFALL_SPEED#)*2.75
									Case OBJTYPE_BFANLOW: p\Motion\Speed\y#=(-p\Physics\FLOATFALL_SPEED#)*1.375
									Default: p\Motion\Speed\y#=(-p\Physics\FLOATFALL_SPEED#)
								End Select
							EndIf
						Case OBJTYPE_SPRING,OBJTYPE_BSPRING,OBJTYPE_SPRINGX,OBJTYPE_SPRINGTRAP,OBJTYPE_SPRINGTRAPX,OBJTYPE_HOOP,OBJTYPE_THOOP,OBJTYPE_ACCEL:
							If p\Action=ACTION_STOMP and (o\InitialRotation\x#=0) Then
								p\Motion\Speed\x# = TFormedX()*(o\Power#+0.25) : p\Motion\Speed\y# = TFormedY()*(o\Power#+0.25) : p\Motion\Speed\z# = TFormedZ()*(o\Power#+0.25)
							Else
								p\Motion\Speed\x# = TFormedX()*(o\Power#) : p\Motion\Speed\y# = TFormedY()*(o\Power#) : p\Motion\Speed\z# = TFormedZ()*(o\Power#)
							EndIf
						Default:
							p\Motion\Speed\x# = TFormedX()*(o\Power#) : p\Motion\Speed\y# = TFormedY()*(o\Power#) : p\Motion\Speed\z# = TFormedZ()*(o\Power#)
					End Select
				EndIf
				p\IceFloorTimer=0
				p\Flags\HomingWasLockedTimer=0

				If p\Action=ACTION_CARRY Or p\Action=ACTION_CARRYJUMP Or p\Action=ACTION_CARRYTHROWN Then
					p\Action=ACTION_CARRYTHROWN
				Else
					Select o\ObjType
						Case OBJTYPE_SPRING,OBJTYPE_BSPRING,OBJTYPE_SPRINGX,OBJTYPE_SPRINGTRAP,OBJTYPE_SPRINGTRAPX,OBJTYPE_HOOP,OBJTYPE_THOOP:
							If p\HasVehicle=0 Then
								p\Action = ACTION_UP
							Else
								Select Game\Vehicle
								Case 1,5,8: p\Action = ACTION_BOARDFALL
								Case 2: p\Action = ACTION_GLIDER
								Case 3,4,9: p\Action = ACTION_CARFALL
								Case 6,7: p\Action = ACTION_TORNADO
								End Select
							EndIf
						Case OBJTYPE_RAMP:
							If p\HasVehicle=0 Then
								p\Action = ACTION_FWD
							Else
								Select Game\Vehicle
								Case 1,5,8: p\Action = ACTION_BOARDFALL
								Case 2: p\Action = ACTION_GLIDER
								Case 3,4,9: p\Action = ACTION_CARFALL
								Case 6,7: p\Action = ACTION_TORNADO
								End Select
							EndIf
						Case OBJTYPE_ACCEL:
							If p\HasVehicle=0 Then
								If p\Flags\InJumpAction and (Not(p\Action=ACTION_JUMPDASH Or p\Action=ACTION_DOUBLEJUMP Or p\Action=ACTION_DIVE)) Then
									Player_ResetJumpActionStuff(p)
								Else
									p\Action = ACTION_JUMPFALL
								EndIf
							Else
								Select Game\Vehicle
								Case 1,5,8: p\Action = ACTION_BOARDFALL
								Case 2: p\Action = ACTION_GLIDER
								Case 3,4,9: p\Action = ACTION_CARFALL
								Case 6,7: p\Action = ACTION_TORNADO
								End Select
							EndIf
						Case OBJTYPE_FAN,OBJTYPE_BFAN,OBJTYPE_BFANLOW:
							Player_ConvertGroundToAir(p)
							p\Motion\Ground = False
							If Not(p\HasVehicle>0 Or p\Action=ACTION_JUMP Or p\Action=ACTION_HOP Or p\Action=ACTION_GLIDE Or p\Action=ACTION_SLOWGLIDE Or p\Action=ACTION_HOVER Or p\Action=ACTION_SOAR Or p\Action=ACTION_SOARFLAP Or (p\Character=CHAR_CHA and p\Action=ACTION_FLY) Or p\Action=ACTION_JUMPDASH Or p\Action=ACTION_HOMING Or p\Action=ACTION_TRANSFORM) Then
								p\Action = ACTION_FLOAT
								Player_ResetJumpActionStuff(p)
							Else
								p\JumpActionRestrictTimer=0.5*secs#
							EndIf
					End Select
				EndIf

				; Rotate Sonic
				Select o\ObjType
					Case OBJTYPE_FAN,OBJTYPE_BFAN,OBJTYPE_BFANLOW:
					Case OBJTYPE_SPRING,OBJTYPE_BSPRING,OBJTYPE_SPRINGX,OBJTYPE_SPRINGTRAP,OBJTYPE_SPRINGTRAPX,OBJTYPE_HOOP,OBJTYPE_THOOP,OBJTYPE_ACCEL:
						If (Not(o\InitialRotation\x#=0)) Then p\Animation\Direction#=o\InitialRotation\y#+180
						p\Physics\UP_ANGLE#=20*p\SpeedLength#
					Case OBJTYPE_PAD,OBJTYPE_FORCER:
						p\Animation\Direction#=o\InitialRotation\y#+180
						If p\HasVehicle=0 Then p\Action=ACTION_COMMON
					Case OBJTYPE_RAMP,OBJTYPE_NODE,OBJTYPE_NODE2:
						p\Animation\Direction#=o\InitialRotation\y#+180
				End Select

				; Transform the position vector to World space
				TFormVector 0, 1, 0, o\Entity, 0
				Select o\ObjType
					Case OBJTYPE_NODE,OBJTYPE_FAN,OBJTYPE_BFAN,OBJTYPE_BFANLOW:
					Case OBJTYPE_BSPRING:
						PositionEntity p\Objects\Entity, o\Position\x#+o\Translator\BigSpringPoint*5.5*Cos(o\Rotation\y#) + TFormedX()*4, o\Position\y# + TFormedY()*4, o\Position\z#+o\Translator\BigSpringPoint*5.5*Sin(o\Rotation\y#) + TFormedZ()*4
					Default:
						PositionEntity p\Objects\Entity, o\Position\x# + TFormedX()*4, o\Position\y# + TFormedY()*4, o\Position\z# + TFormedZ()*4
				End Select
				p\Physics\MOTION_GROUND# = 0.65
				Player_Motion_ResetAirBeg(p)
			EndIf

			; Add to counter
			If o\ObjType=OBJTYPE_THOOP And o\Translator\THoopPointDisabler = 0 Then 
				Gameplay_AddScore(100)
				o\Translator\THoopPointDisabler = 1
			EndIf
				
			; Sound effect!
			Select o\ObjType
				Case OBJTYPE_SPRING,OBJTYPE_BSPRING,OBJTYPE_SPRINGX:
					EmitSmartSound(Sound_Spring,o\Entity)
				Case OBJTYPE_PAD:
					EmitSmartSound(Sound_DashPad,o\Entity)
				Case OBJTYPE_RAMP:
					EmitSmartSound(Sound_DashRamp,o\Entity)
				Case OBJTYPE_HOOP:
					EmitSmartSound(Sound_HoopDash,o\Entity) : p\Physics\TRICK_ANGLE#=360
				Case OBJTYPE_THOOP:
					EmitSmartSound(Sound_HoopRainbow,o\Entity) : p\Physics\TRICK_ANGLE#=360 : p\TrickTimer=1*secs#
				Case OBJTYPE_ACCEL:
					EmitSmartSound(Sound_HoopDash,o\Entity)
				Case OBJTYPE_SPRINGTRAP,OBJTYPE_SPRINGTRAPX:
					EmitSmartSound(Sound_SpringTrap,o\Entity)
			End Select
				
			; Apply motion blur
			Select o\ObjType
				Case OBJTYPE_LOCKER,OBJTYPE_FORCER,OBJTYPE_NODE,OBJTYPE_NODE2,OBJTYPE_FAN,OBJTYPE_BFAN,OBJTYPE_BFANLOW:
				Default: PostEffect_Create_MotionBlur(0.85)
					 PositionEntity(o\IValues[0],p\Objects\Position\x#,p\Objects\Position\y#,p\Objects\Position\z#)
			End Select

			; Save stuck
			Select o\ObjType
				Case OBJTYPE_LOCKER,OBJTYPE_FORCER,OBJTYPE_NODE,OBJTYPE_NODE2,OBJTYPE_FAN,OBJTYPE_BFAN,OBJTYPE_BFANLOW,OBJTYPE_ACCEL:
				Default: If Game\Vehicle=0 Then Input_ResetActionInput()
			End Select

			; Do lock
			Object_LockCamera(o,p)
			Object_LockDestination(o,p)

			; Initiate restricting timer
			Select o\ObjType
				Case OBJTYPE_LOCKER,OBJTYPE_FAN,OBJTYPE_BFAN,OBJTYPE_BFANLOW:
				Default:
					Select o\ObjType
						Case OBJTYPE_ACCEL: o\Translator\WasJustUsedTimer=0.225*secs#
						Default: o\Translator\WasJustUsedTimer=0.1*secs#
					End Select
					p\TranslatorsTouchedTimer=0.1*secs#
					p\TranslatorsTouched=p\TranslatorsTouched+1
			End Select

		EndIf
		
		;Quad effect
		Object_QuadEffect(o,p,d)
		
	End Function

; /\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/
; /\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/

	Function Object_Bumper_Create.tObject(x#, y#, z#, pitch#, yaw#, roll#, paddletype#=0)
		o.tObject = New tObject : o\ObjType = TempAttribute\ObjectNo : o\ID=TempAttribute\ObjectID
		If o\ObjType=OBJTYPE_BALLBUMPER Then o\CanHoming=True Else o\CanHoming=False
		o\ThisIsABumper=True : o\Translator = New tObject_Translator : o\HasValuesetTranslator=True

		Select o\ObjType
			Case OBJTYPE_BALLBUMPER: Object_CreateHitBox(HITBOXTYPE_SPEEDY,o,6.7,6.7,6.7)
			Case OBJTYPE_GROUNDBUMPER: Object_CreateHitBox(HITBOXTYPE_SPEEDY,o,5.7,5.7,5.7)
			Case OBJTYPE_METROBUMPER: Object_CreateHitBox(HITBOXTYPE_SPEEDY,o,7.7,7.7,7.7)
			Case OBJTYPE_PLATEBUMPER: Object_CreateHitBox(HITBOXTYPE_SPEEDY,o,4.8,4.8,4.8)
			Case OBJTYPE_TRIANGLEBUMPER: Object_CreateHitBox(HITBOXTYPE_SPEEDY,o,12.2,12.2,12.2)
			Case OBJTYPE_PADDLE: Object_CreateHitBox(HITBOXTYPE_SPEEDY,o,16,14,16)
		End Select

		Object_Acquire_Position(o,x#,y#,z#)
		Object_Acquire_Rotation(o,pitch#,yaw#,roll#)

		Select o\ObjType
			Case OBJTYPE_BALLBUMPER:
				o\Entity = CopyEntity(MESHES(SmartEntity(Mesh_BallBumperOn)), Game\Stage\Root)
				o\Entity2 = CopyEntity(MESHES(SmartEntity(Mesh_BallBumperOff)), Game\Stage\Root)
			Case OBJTYPE_GROUNDBUMPER:
				o\Entity = CopyEntity(MESHES(SmartEntity(Mesh_GroundBumperOn)), Game\Stage\Root)
				o\Entity2 = CopyEntity(MESHES(SmartEntity(Mesh_GroundBumperOff)), Game\Stage\Root)
			Case OBJTYPE_METROBUMPER:
				o\Entity = CopyEntity(MESHES(SmartEntity(Mesh_MetroBumper1)), Game\Stage\Root)
				o\Entity2 = CopyEntity(MESHES(SmartEntity(Mesh_MetroBumper2)), Game\Stage\Root)
			Case OBJTYPE_PLATEBUMPER:
				o\Entity = LoadAnimMesh("Objects/Bumpers/Plate3.b3d", Game\Stage\Root) : ScaleEntity o\Entity,0.5,0.5,0.5
				o\Entity2 = LoadAnimMesh("Objects/Bumpers/Plate2.b3d", Game\Stage\Root) : ScaleEntity o\Entity2,0.5,0.5,0.5
				o\Entity3 = LoadAnimMesh("Objects/Bumpers/Plate1.b3d", Game\Stage\Root) : ScaleEntity o\Entity3,0.5,0.5,0.5
				o\Entity4 = LoadAnimMesh("Objects/Bumpers/PlateX.b3d", Game\Stage\Root) : ScaleEntity o\Entity4,0.5,0.5,0.5
				EntityType(o\Entity,COLLISION_WORLD_POLYGON_PINBALL)
				EntityType(o\Entity2,COLLISION_WORLD_POLYGON_PINBALL)
				EntityType(o\Entity3,COLLISION_WORLD_POLYGON_PINBALL)
			Case OBJTYPE_TRIANGLEBUMPER:
				o\Entity = CopyEntity(MESHES(SmartEntity(Mesh_TriangleBumper)), Game\Stage\Root)
			Case OBJTYPE_PADDLE:
				o\Entity = CopyEntity(MESHES(SmartEntity(Mesh_Paddle+paddletype#)), Game\Stage\Root)
				EntityType(o\Entity,COLLISION_WORLD_POLYGON_PINBALL)
		End Select

		Return o
	End Function
	
	; =========================================================================================================
	
	Function Object_Bumper_Appearance(o.tObject, d.tDeltaTime)
		Select o\ObjType
			Case OBJTYPE_BALLBUMPER,OBJTYPE_GROUNDBUMPER:
				TurnEntity o\Entity, 0, -0.15*20*d\Delta, 0
				TurnEntity o\Entity2, 0, -0.15*20*d\Delta, 0
			Case OBJTYPE_PLATEBUMPER:
				TurnEntity o\Entity4, 0, -0.10*20*d\Delta, 0
		End Select
		Select o\ObjType
			Case OBJTYPE_BALLBUMPER,OBJTYPE_GROUNDBUMPER,OBJTYPE_METROBUMPER:
				If o\Translator\BumperTimer>0 Then
					o\Translator\BumperTimer=o\Translator\BumperTimer-timervalue#
				Else
					o\Translator\BumperTimer=0.7*secs#
				EndIf

				If o\Translator\BumperTimer>0.35*secs# Then
					ShowEntity(o\Entity)
					HideEntity(o\Entity2)
				ElseIf o\Translator\BumperTimer<0.35*secs# Then
					ShowEntity(o\Entity2)
					HideEntity(o\Entity)
				EndIf
			Case OBJTYPE_PLATEBUMPER
				If o\Translator\PlateBumper=0 Then
					ShowEntity(o\Entity)
					HideEntity(o\Entity2)
					HideEntity(o\Entity3)
				ElseIf o\Translator\PlateBumper=1 Then
					HideEntity(o\Entity)
					ShowEntity(o\Entity2)
					HideEntity(o\Entity3)
				ElseIf o\Translator\PlateBumper=2 Then
					HideEntity(o\Entity)
					HideEntity(o\Entity2)
					ShowEntity(o\Entity3)
				EndIf
				ShowEntity(o\Entity4)
			Case OBJTYPE_TRIANGLEBUMPER,OBJTYPE_PADDLE
				ShowEntity(o\Entity)
		End Select
	End Function

	Function Object_Bumper_Update(o.tObject, p.tPlayer, d.tDeltaTime)

		; Availability
		If o\Translator\BumperCoolDownTimer>0 Then o\Translator\BumperCoolDownTimer=o\Translator\BumperCoolDownTimer-timervalue#

		; Movement
		Select o\ObjType
			Case OBJTYPE_PADDLE:
				Select o\Translator\CannonWasUsedTimer
					Case 0:
						Animate(o\Entity,1,0,1)
						o\Translator\BumperCoolDownTimer=0.1*secs#
						If p\Action=ACTION_BUMPED Then
							If Input\Pressed\ActionJump Then
								o\Translator\CannonWasUsedTimer=1 : o\State=1
								Animate(o\Entity,3,0.975,1)
								EmitSmartSound(Sound_Paddle,o\Entity)
								o\Translator\BumperCoolDownTimer=0
							EndIf
						EndIf
					Case 1:
						If Not(Animating(o\Entity)) Then o\Translator\CannonWasUsedTimer=2
						If (Not(Input\Hold\ActionJump)) Or (Not(p\Action=ACTION_BUMPED)) Then o\Translator\CannonWasUsedTimer=0 : o\State=0
					Case 2:
						o\Translator\BumperCoolDownTimer=0.1*secs#
						If (Not(Input\Hold\ActionJump)) Or (Not(p\Action=ACTION_BUMPED)) Then o\Translator\CannonWasUsedTimer=0 : o\State=0
				End Select
		End Select

		; Appearance
		Object_Bumper_Appearance(o,d)
		
		; Player collided with object
		If (Not(o\Translator\BumperCoolDownTimer>0)) And o\Hit and (Not(p\TranslatorsTouchedTimer>0)) Then
			Select o\ObjType
				Case OBJTYPE_BALLBUMPER,OBJTYPE_GROUNDBUMPER,OBJTYPE_PLATEBUMPER: o\State = 1 : o\FValues[0] = 40.0
				Case OBJTYPE_METROBUMPER,OBJTYPE_TRIANGLEBUMPER: o\State = 1 : o\FValues[0] = 0.0
			End Select

			If Not(p\Action=ACTION_HURT Or p\Action=ACTION_DIE) Then
				; Make Sonic a move
				x# = o\Position\x# - p\Objects\Position\x#
				y# = o\Position\y# - p\Objects\Position\y#
				z# = o\Position\z# - p\Objects\Position\z#

				l# = Sqr(x^2 + y^2 + z^2)*1.5

				TFormVector x/l, y/l, z/l, 0, p\Objects\Entity

				Select o\ObjType
					Case OBJTYPE_BALLBUMPER:
						If p\Action=ACTION_JUMP Or p\Action=ACTION_JUMPFALL Or p\Action=ACTION_HOMING Or p\Action=ACTION_JUMPDASH Or p\Action=ACTION_DASH Then
							Player_Action_BumpedJump_Initiate(p)
							Player_SetSpeed(p,0)
							p\Motion\Speed\y# = 1.85
							p\Motion\Ground = False
						Else
							Object_Bumper_Update_Bumped(o,p,4.7)
						EndIf
					Case OBJTYPE_GROUNDBUMPER:
						Object_Bumper_Update_Bumped(o,p,4.7)
					Case OBJTYPE_METROBUMPER:
						Object_Bumper_Update_Bumped(o,p,6.4,false)
					Case OBJTYPE_PLATEBUMPER:
						If o\Rotation\x#=0 Then
							Player_Action_BumpedJump_Initiate(p)
							Player_SetSpeed(p,0)
							If p\Motion\Speed\y#<=0 Then p\Motion\Speed\y# = 1.14 Else p\Motion\Speed\y# = -1.14
							p\Motion\Ground = False
						Else
							Object_Bumper_Update_Bumped(o,p,3.14)
						EndIf
					Case OBJTYPE_TRIANGLEBUMPER:
						Object_Bumper_Update_Bumped(o,p,8.1)
					Case OBJTYPE_PADDLE:
						If o\Translator\CannonWasUsedTimer=1 Then
							Object_Bumper_Update_Bumped(o,p,12.4)
						EndIf
				End Select
			EndIf
			Player_ResetJumpActionStuff(p)
			p\IceFloorTimer=0
			p\Flags\HomingWasLockedTimer=0

			; Add to counter
			Gameplay_AddScore(10)

			; Update cool down timer
			o\Translator\BumperCoolDownTimer=0.1*secs#
			p\TranslatorsTouchedTimer=0.2*secs#
				
			; Sound effect!
			Select o\ObjType
				Case OBJTYPE_BALLBUMPER: EmitSmartSound(Sound_Bumper1,o\Entity)
				Case OBJTYPE_GROUNDBUMPER: EmitSmartSound(Sound_Bumper1,o\Entity)
				Case OBJTYPE_METROBUMPER: EmitSmartSound(Sound_Bumper2,o\Entity)
				Case OBJTYPE_PLATEBUMPER: EmitSmartSound(Sound_Bumper4,o\Entity)
				Case OBJTYPE_TRIANGLEBUMPER: EmitSmartSound(Sound_Bumper3,o\Entity)
				Case OBJTYPE_PADDLE: EmitSmartSound(Sound_Paddle2,o\Entity)
			End Select
				
			; Apply motion blur
			PostEffect_Create_MotionBlur(0.85)

			; Update bumper plate
			If o\ObjType=OBJTYPE_PLATEBUMPER Then
				o\Translator\PlateBumper=o\Translator\PlateBumper+1
				If o\Translator\PlateBumper=3 Then
					o\Done=1
					Return
				EndIf
			EndIf
		EndIf
		
		;Quad effect
		If o\State = 1 Then
			o\AlwaysPresent=True
			Select o\ObjType
				Case OBJTYPE_BALLBUMPER,OBJTYPE_GROUNDBUMPER,OBJTYPE_PLATEBUMPER: o\FValues[0] = o\FValues[0] + 18*d\Delta
				Case OBJTYPE_METROBUMPER,OBJTYPE_TRIANGLEBUMPER: o\FValues[0] = o\FValues[0] + 0.05*d\Delta
			End Select

			; Wooble effect
			Select o\ObjType
				Case OBJTYPE_BALLBUMPER:
					ScaleEntity o\Entity, 0.5-0.5*Cos(o\FValues[0])/(o\FValues[0]*0.01), 0.5-0.5*Sin(o\FValues[0])/(o\FValues[0]*0.01), 0.5-0.5*Sin(o\FValues[0])/(o\FValues[0]*0.01)
					ScaleEntity o\Entity2, 0.5-0.5*Cos(o\FValues[0])/(o\FValues[0]*0.01), 0.5-0.5*Sin(o\FValues[0])/(o\FValues[0]*0.01), 0.5-0.5*Sin(o\FValues[0])/(o\FValues[0]*0.01)
				Case OBJTYPE_GROUNDBUMPER:
					ScaleEntity o\Entity, 0.5-0.5*Cos(o\FValues[0])/(o\FValues[0]*0.01), 0.5, 0.5-0.5*Sin(o\FValues[0])/(o\FValues[0]*0.01)
					ScaleEntity o\Entity2, 0.5-0.5*Cos(o\FValues[0])/(o\FValues[0]*0.01), 0.5, 0.5-0.5*Sin(o\FValues[0])/(o\FValues[0]*0.01)
				Case OBJTYPE_METROBUMPER:
					ScaleEntity o\Entity, 0.654, 0.654 + Sin (o\FValues[0]*180)*0.654, 0.654
					ScaleEntity o\Entity2, 0.654, 0.654 + Sin (o\FValues[0]*180)*0.654, 0.654
				Case OBJTYPE_TRIANGLEBUMPER:
					ScaleEntity o\Entity, 0.55, 0.55 + Sin (o\FValues[0]*180)*0.55, 0.55
			End Select

			Select o\ObjType
				Case OBJTYPE_BALLBUMPER,OBJTYPE_GROUNDBUMPER,OBJTYPE_PLATEBUMPER:
					If o\FValues[0] >= 4500 Then
						o\State = 0
						o\FValues[0] = 0.0
					EndIf
				Case OBJTYPE_METROBUMPER,OBJTYPE_TRIANGLEBUMPER:
					If o\FValues[0] >= 1.0 Then
						o\State = 0
						o\FValues[0] = 0.0
						ScaleEntity o\Entity, 0.5, 0.5, 0.5
						If o\Entity2<>0 Then ScaleEntity o\Entity2, 0.5, 0.5, 0.5
					EndIf
			End Select
			If o\State=0 Then o\AlwaysPresent=False
		EndIf
		
	End Function

	Function Object_Bumper_Update_Bumped(o.tObject, p.tPlayer, power#, vertical=true)
		Player_Action_Bumped_Initiate(p)
		If EntityDistance(p\Objects\Entity,o\Entity)>0.1 Then
			p\Motion\Speed\x# = p\Motion\Speed\x#-TFormedX()*power#
			If vertical and p\Motion\Ground=False Then p\Motion\Speed\y# = p\Motion\Speed\y#-TFormedY()*power#
			p\Motion\Speed\z# = p\Motion\Speed\z#-TFormedZ()*power#
			p\Motion\Ground = False
		EndIf
	End Function

; /\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/
; /\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/

	Function Object_Cannon_Create.tObject(x#, y#, z#, pitch#, yaw#, roll#, power#)
		o.tObject = New tObject : o\ObjType = TempAttribute\ObjectNo : o\ID=TempAttribute\ObjectID
		o\Translator = New tObject_Translator : o\HasValuesetTranslator=True

		Object_CreateHitBox(HITBOXTYPE_NORMAL,o,12.4,12.4,12.4)

		Object_Acquire_Position(o,x#,y#,z#)
		Object_Acquire_Rotation(o,0,yaw#,0)
		Object_Acquire_Power(o,power#)

		o\State=0

		o\Entity = CopyEntity(MESHES(SmartEntity(Mesh_Cannon)), Game\Stage\Root)

		Object_CreateQuad(o)

		Return o
	End Function
	
	; =========================================================================================================
	
	Function Object_Cannon_Update(o.tObject, p.tPlayer, d.tDeltaTime)

		If o\Translator\CannonWasUsedTimer>0 Then o\Translator\CannonWasUsedTimer=o\Translator\CannonWasUsedTimer-timervalue#

		; Player collided with object
		If (o\Rotation\x#>0) and (Not(p\Action=ACTION_CANNON Or p\Action=ACTION_CANNON2)) Then
			TurnEntity o\Entity,-2.75*d\Delta,0,0
			Player_Motion_ResetAirBeg(p)
		ElseIf (Not(o\Translator\CannonWasUsedTimer>0)) Then
			If o\Hit Then
			Select p\Action
				Case ACTION_CANNON:
					If Input\Hold\Left Then TurnEntity o\Entity,0,2.5*d\Delta,0,1
					If Input\Hold\Right Then TurnEntity o\Entity,0,-2.5*d\Delta,0,1
					cam\Rotation\x#=10
					cam\TargetRotation\x#=10
					cam\Rotation\y#=o\Rotation\y#
					cam\TargetRotation\y#=o\Rotation\y#
					If Not(o\Rotation\x#>=45) Then
						TurnEntity o\Entity,2.75*d\Delta,0,0
					Else
						If Input\Pressed\ActionJump Or Input\Pressed\ActionRoll Then p\Action=ACTION_CANNON2
					EndIf
				Case ACTION_CANNON2:
					o\State = 1
					o\FValues[0] = 0.0

					; Transform the Speed vector to the Player space
					Player_Align(p)
					ScaleEntity o\Entity, 1.5, 1.5, 1.5
					TFormVector 0, 1, 0, o\Entity, p\Objects\Entity

					; Make Sonic a move
					p\Motion\Ground = False
					If o\Power#>0 Then
						p\Motion\Speed\x# = TFormedX()*(o\Power#) : p\Motion\Speed\y# = TFormedY()*(o\Power#) : p\Motion\Speed\z# = TFormedZ()*(o\Power#)
					EndIf

					p\Action = ACTION_CANNON3

					; Rotate Sonic
					p\Animation\Direction#=o\Rotation\y#+180
					If p\Animation\Direction#<=180 Then
						cam\Rotation\y#=p\Animation\Direction#+180
						cam\TargetRotation\y#=p\Animation\Direction#+180
					Else
						cam\Rotation\y#=p\Animation\Direction#-180
						cam\TargetRotation\y#=p\Animation\Direction#-180
					EndIf
					p\Physics\UP_ANGLE#=20*p\SpeedLength#

					; Transform the position vector to World space
					TFormVector 0, 1, 0, o\Entity, 0
					PositionEntity p\Objects\Entity, o\Position\x# + TFormedX()*4, o\Position\y# + TFormedY()*4, o\Position\z# + TFormedZ()*4
					p\Physics\MOTION_GROUND# = 0.65
				
					; Sound effect!
					EmitSmartSound(Sound_Cannon,o\Entity)
					o\Translator\CannonWasUsedTimer=0.5*secs#
				
					; Apply motion blur
					PostEffect_Create_MotionBlur(0.85)
					PositionEntity(o\IValues[0],p\Objects\Position\x#,p\Objects\Position\y#,p\Objects\Position\z#)

					; Save stuck
					Input\Pressed\ActionJump=False
					Input\Pressed\ActionRoll=False
				Case ACTION_CANNON3:
				Default:
					If (Not(p\Action=ACTION_HURT Or p\Action=ACTION_DIE)) Then
						p\CannonX#=o\Position\x#
						p\CannonY#=o\Position\y#
						p\CannonZ#=o\Position\z#
						Player_Align(p)
						p\Action=ACTION_CANNON
						Input\Pressed\ActionJump=False
						Input\Pressed\ActionRoll=False
						Game\Vehicle=0
						p\IceFloorTimer=0
						p\Flags\HomingWasLockedTimer=0
					EndIf
			End Select
			EndIf
		EndIf
		
		;Quad effect
		Object_QuadEffect(o,p,d)
		
	End Function

; /\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/
; /\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/

	Function Object_Transferer_Create.tObject(x#, y#, z#, pitch#, yaw#, roll#, power#, inverted#=0)
		o.tObject = New tObject : o\ObjType = TempAttribute\ObjectNo : o\ID=TempAttribute\ObjectID
		o\Translator = New tObject_Translator : o\HasValuesetTranslator=True

		Object_CreateHitBox(HITBOXTYPE_SPEEDY_TRANSFERER,o,7.875,5.25,7.875)

		Object_Acquire_Position(o,x#,y#,z#)
		Object_Acquire_Rotation(o,0,yaw#,0)
		Object_Acquire_Power(o,power#)
		Select o\ObjType
			Case OBJTYPE_PROPELLER,OBJTYPE_ROCKET:
				Object_Acquire_Destination(o,1,TempAttribute\dx#,TempAttribute\dy#,TempAttribute\dz#)
			Case OBJTYPE_PULLEY,OBJTYPE_ELEVATOR:
				Object_Acquire_Destination(o,1,x#,TempAttribute\dy#,z#)
		End Select

		Select o\ObjType
			Case OBJTYPE_PROPELLER:
				o\Entity = CopyEntity(MESHES(SmartEntity(Mesh_Propeller)), Game\Stage\Root)
				Animate(o\Entity,1,0.45,1)
			Case OBJTYPE_PULLEY:
				o\Entity = CopyEntity(MESHES(SmartEntity(Mesh_Pulley)), Game\Stage\Root)
				o\Entity2 = CopyEntity(MESHES(SmartEntity(Mesh_PulleyBase)), Game\Stage\Root)
				o\EntityX = FindChild(o\Entity, "Rope")
				o\Translator\TransfererInverted=inverted#
			Case OBJTYPE_ROCKET:
				o\Entity = CopyEntity(MESHES(SmartEntity(Mesh_Rocket)), Game\Stage\Root)
				o\Entity2 = CopyEntity(MESHES(SmartEntity(Mesh_RocketBase)), Game\Stage\Root)
				o\EntityX = FindChild(o\Entity, "jet")
			Case OBJTYPE_ELEVATOR:
				o\Entity = CopyEntity(MESHES(SmartEntity(Mesh_Empty)), Game\Stage\Root)
				o\Entity2 = CopyEntity(MESHES(SmartEntity(Mesh_Elevator)), Game\Stage\Root)
				o\ExtraTexture=LoadTexture("Objects\Springs\elevator.png", 1+256) : o\HasExtraTexture=1
				ApplyMeshTextureLayer(o\Entity2, "elevator.png", o\ExtraTexture)
		End Select

		o\Entity3 = CreatePivot()
		o\Entity4 = CreatePivot() : EntityType(o\Entity4,COLLISION_OBJECT2)

		Return o
	End Function
	
	; =========================================================================================================
	
	Function Object_Transferer_UpdateAlways(o.tObject, p.tPlayer, d.tDeltaTime)
		; Place destination
		If o\Translator\TransfererInverted=0 Then
			If o\Translator\TransfererPlace Then i=1 Else i=0
		Else
			If o\Translator\TransfererPlace Then i=0 Else i=1
		EndIf
		Select i
			Case 1: PositionEntity o\Entity3, o\Translator\Destination\x#, o\Translator\Destination\y#, o\Translator\Destination\z#, 1
			Case 0: PositionEntity o\Entity3, o\InitialPosition\x#, o\InitialPosition\y#, o\InitialPosition\z#, 1
		End Select

		; Force destination if told to do so
		If o\Translator\TransfererDone=-1 Then
			PositionEntity o\Entity4, EntityX(o\Entity3), EntityY(o\Entity3), EntityZ(o\Entity3), 1
			o\Translator\TransfererDone=0
			o\AlwaysPresent=False
		EndIf

		; If destination checker farther than destination, then take it to destination
		If EntityDistance(o\Entity4,o\Entity3)>0 Then
			PointEntity o\Entity4, o\Entity3
			If o\ObjType=OBJTYPE_ROCKET Then PointEntity o\Entity, o\Entity3

			; Move to destination
			If EntityDistance(o\Entity4,o\Entity3)>o\Power# Then
				MoveEntity o\Entity4, 0, 0, o\Power#*d\Delta
				Select o\ObjType
					Case OBJTYPE_ROCKET:
						Object_RocketEffect(o,o\EntityX)
					Case OBJTYPE_PULLEY:
						o\Translator\PulleyReturnTimer=4*secs#
					Case OBJTYPE_ELEVATOR:
						If (p\Action=ACTION_HOLD and o\Hit) Then
							p\BumpedCloudTimer=0.5*secs#
							ParticleTemplate_Call(p\SmokeParticle, PARTICLE_PLAYER_CHARACTERDUST, p\Objects\Mesh, 0, 0, 0, p\RealCharacter)
							Game\ControlLock=0.1*secs#
						EndIf
				End Select

				If Not(p\Action=ACTION_HOLD) Then o\Translator\TransfererIsBeingUsed=False

			; Almost reached destination
			Else
				MoveEntity o\Entity4, 0, 0, EntityDistance(o\Entity4,o\Entity3)*d\Delta
				If (p\Action=ACTION_HOLD and o\Hit) Then holding=true Else holding=false
				If o\Translator\TransfererDone=0 and (holding Or ((Not(p\Action=ACTION_HOLD)) and (o\ObjType=OBJTYPE_ROCKET Or o\ObjType=OBJTYPE_ELEVATOR))) Then
					If holding Then
						p\Action=ACTION_JUMPFALL : p\ShouldBeHoldingTimer=0.75*secs#
						Select o\ObjType
							Case OBJTYPE_ELEVATOR: EmitSmartSound(Sound_Elevator,o\Entity)
							Default: EmitSmartSound(Sound_Grab,o\Entity)
						End Select
					EndIf
					Select o\ObjType
						Case OBJTYPE_PULLEY:
							If ChannelPlaying(o\Translator\Channel_Fan) Then StopChannel(o\Translator\Channel_Fan)
							If o\Translator\TransfererPlace=0 Then Player_ActuallyJump(p,true)
						Case OBJTYPE_ROCKET:
							If ChannelPlaying(o\Translator\Channel_Fan) Then StopChannel(o\Translator\Channel_Fan)
							ParticleTemplate_Call(o\Particle, PARTICLE_OBJECT_ROCKETEXPLODE, o\Entity) : EmitSmartSound(Sound_Explode,p\Objects\Entity)
							o\Translator\TransfererDone=1
							Objects_Reset_Object(o)
						Case OBJTYPE_ELEVATOR:
							If ChannelPlaying(o\Translator\Channel_Fan) Then StopChannel(o\Translator\Channel_Fan)
							o\Translator\TransfererDone=1
							Objects_Reset_Object(o)
					End Select
					o\Translator\TransfererForcePlace=0
					o\Translator\TransfererIsBeingUsed=False
					o\AlwaysPresent=False
				EndIf
			EndIf

			; Continuous sound
			If o\Inview and EntityDistance(o\Entity4,o\Entity3)>0.5 and o\Translator\TransfererForcePlace=0 and ChannelPlaying(o\Translator\Channel_Fan)=False and o\Translator\TransfererDone=0 Then
				Select o\ObjType
					Case OBJTYPE_PULLEY: o\Translator\Channel_Fan=EmitSmartSound(Sound_Grabber,o\Entity)
					Case OBJTYPE_ROCKET: o\Translator\Channel_Fan=EmitSmartSound(Sound_Rocket,o\Entity)
				End Select
			EndIf
		EndIf

		; Extra stuff
		Select o\ObjType
			Case OBJTYPE_PULLEY:
				RotateEntity o\Entity, 0, o\Rotation\y#, 0
				PositionEntity o\EntityX, EntityX(o\Entity2), EntityY(o\Entity2), EntityZ(o\Entity2), 1
				If o\Translator\PulleyReturnTimer>0 Then
					o\Translator\PulleyReturnTimer=o\Translator\PulleyReturnTimer-timervalue#
				Else
					If o\Translator\TransfererPlace=0 Then o\Translator\TransfererPlace=1 : o\Translator\TransfererForcePlace=1
				EndIf
			Case OBJTYPE_ROCKET:
				If o\Translator\TransfererDone=1 Then HideEntity(o\Entity)
			Case OBJTYPE_ELEVATOR:
				PositionTexture(o\ExtraTexture, 0, 0.0007*Game\Gameplay\Time)
		End Select

		If Not( o\Hit and o\Translator\TransfererDone=0 and ( (Not(o\ObjType=OBJTYPE_PULLEY)) Or (o\Translator\TransfererPlace=1 Or p\Motion\Ground=False) ) )Then
			PositionEntity o\Entity, EntityX(o\Entity4), EntityY(o\Entity4), EntityZ(o\Entity4), 1
		EndIf
	End Function

	Function Object_Transferer_Update(o.tObject, p.tPlayer, d.tDeltaTime)

		Object_Transferer_UpdateAlways(o,p,d)

		; For fan
		If o\ObjType=OBJTYPE_PROPELLER and ChannelPlaying(o\Translator\Channel_Fan)=False Then o\Translator\Channel_Fan=EmitSmartSound(Sound_Propel,o\Entity)

		; Player collided with object
		If (o\Hit Or o\Translator\TransfererIsBeingUsed) and o\Translator\TransfererDone=0 and ( (Not(o\ObjType=OBJTYPE_PULLEY)) Or (o\Translator\TransfererPlace=1 Or p\Motion\Ground=False) ) Then
			; If player is already holding
			If p\Action=ACTION_HOLD Then
				; Player's speed and direction
				Player_SetSpeed(p,0) : Player_SetSpeedY(p,0)
				Select o\ObjType
					Case OBJTYPE_ROCKET: p\Animation\Direction#=o\Rotation\y#+180
					Default: p\Animation\Direction#=o\Rotation\y#
				End Select

				; Place holding obj at player
				PositionEntity o\Entity, p\Objects\Position\x#, p\Objects\Position\y#, p\Objects\Position\z#, 1

				; Place player at destination checker
				PositionEntity p\Objects\Entity, EntityX(o\Entity4), EntityY(o\Entity4), EntityZ(o\Entity4), 1
				Player_FollowerHolding_ByFeet(p,true)

				; Fix position of transferer
				Select o\ObjType
					Case OBJTYPE_PULLEY: MoveEntity o\Entity, 0, 5.0+p\ScaleFactor#, 0
					Default: MoveEntity o\Entity, 0, 0.5+p\ScaleFactor#, 0
				End Select

				Player_Motion_ResetAirBeg(p)
				p\ShouldBeHoldingTimer=0.1*secs#

			; If player is not holding yet
			ElseIf (Not(p\ShouldBeHoldingTimer>0)) and o\Translator\TransfererPlace<2 Then
				If (Not(EntityDistance(o\Entity4,o\Entity3)>0.5)) Then o\Translator\TransfererPlace=abs(o\Translator\TransfererPlace-1)
				p\Action=ACTION_HOLD : o\Translator\TransfererIsBeingUsed=True
				Game\Vehicle=0
				p\IceFloorTimer=0
				p\Flags\HomingWasLockedTimer=0
				Player_SetSpeed(p,0) : Player_SetSpeedY(p,0)
				Player_PlayAttackVoice(p)
				PositionEntity p\Objects\Entity, EntityX(o\Entity), EntityY(o\Entity), EntityZ(o\Entity), 1
				p\ShouldBeHoldingTimer=0.1*secs# : p\IsHoldingTimer=0.5*secs#
				Select o\ObjType
					Case OBJTYPE_ELEVATOR:
					Default:
						EmitSmartSound(Sound_Grab,o\Entity)
						p\JustGrabbedPulleyTimer=0.25*secs#
				End Select
				Select o\ObjType
					Case OBJTYPE_ROCKET: EmitSmartSound(Sound_RocketGo,o\Entity)
					Case OBJTYPE_ELEVATOR: o\Translator\Channel_Fan=EmitSmartSound(Sound_ElevatorCharge,o\Entity)
				End Select
				o\Translator\TransfererForcePlace=0
			EndIf
			o\AlwaysPresent=True
		Else
			PositionEntity o\Entity, EntityX(o\Entity4), EntityY(o\Entity4), EntityZ(o\Entity4), 1
		EndIf
		
	End Function

; /\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/
; /\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/

	Function Object_Trigger_Create.tObject(x#, y#, z#, special#=0, size#=0)
		o.tObject = New tObject : o\ObjType = TempAttribute\ObjectNo : o\ID=TempAttribute\ObjectID

		Select o\ObjType
		Case OBJTYPE_TRIGGER_WATER,OBJTYPE_TRIGGER_MUSIC: Object_CreateHitBox(HITBOXTYPE_SPEEDY,o,size#,size#,size#)
		Default: Object_CreateHitBox(HITBOXTYPE_SPEEDY,o,20,20,20)
		End Select

		Object_Acquire_Position(o,x#,y#,z#)

		If (Menu\Settings\Debug#=1 and Menu\Settings\DebugNodes#=1) Then
			o\Entity = CopyEntity(MESHES(SmartEntity(Mesh_Point)), Game\Stage\Root)
		Else
			o\Entity = CreatePivot()
		EndIf

		o\Mode=special#

		Return o
	End Function
	
	; =========================================================================================================
	
	Function Object_Trigger_Update(o.tObject, p.tPlayer)

		; Player collided with object
		If o\Hit Then
			Select o\ObjType
				Case OBJTYPE_TRIGGER_VEHICLECANCEL:
					If Game\Vehicle>0 Then Game\Vehicle=0 : Input_ResetActionInput()
				Case OBJTYPE_TRIGGER_MACH:
					Game\MachLockTriggered=1
				Case OBJTYPE_TRIGGER_MACHCANCEL:
					If Game\MachLockTriggered=1 Then Game\MachLockTriggered=0 : Game\MachLock=0
				Case OBJTYPE_TRIGGER_SKYDIVE:
					p\Action=ACTION_SKYDIVE
					Game\Vehicle=0
				Case OBJTYPE_TRIGGER_SKYDIVECANCEL:
					If p\Action=ACTION_SKYDIVE Then p\Action=ACTION_FALL
				Case OBJTYPE_TRIGGER_WATER:
					p\UnderwaterTriggerTimer=0.2*secs#
				Case OBJTYPE_TRIGGER_MUSIC:
					If Not(Game\Stage\Properties\MusicMode=o\Mode) Then
						Game\Stage\Properties\MusicMode=o\Mode
						StopChannel(Game\Stage\Properties\MusicChn[o\Mode])
						For i=0 to 2
							If i=o\Mode Then
								Game\Stage\Properties\MusicFade#[i]=0.0
							Else
								Game\Stage\Properties\MusicFade#[i]=1.0
							EndIf
						Next
					EndIf
			End Select
		EndIf
		
	End Function

; /\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/
; /\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/

	Function Object_FPlat_Create.tObject(x#, y#, z#, pitch#, yaw#, roll#)
		o.tObject = New tObject : o\ObjType = TempAttribute\ObjectNo : o\ID=TempAttribute\ObjectID

		Object_CreateHitBox(HITBOXTYPE_NORMAL,o,10,5,10)

		Object_Acquire_Position(o,x#,y#,z#)
		Object_Acquire_Rotation(o,0,yaw#,0)

		o\Entity = CreatePivot()
		EntityRadius(o\Entity,5)
		EntityType(o\Entity,COLLISION_OBJECT)
		o\EntityX = CopyEntity(MESHES(SmartEntity(Mesh_Platform)), Game\Stage\Root)
		EntityType(o\EntityX,COLLISION_WORLD_POLYGON)

		Return o
	End Function
	
	; =========================================================================================================
	
	Function Object_FPlat_Update(o.tObject, p.tPlayer, d.tDeltaTime)

		If o\Mode>0 Then o\Mode=o\Mode-timervalue#

		PositionEntity o\EntityX, o\Position\x#, o\Position\y#, o\Position\z#, 1

		Select o\State
			Case 0:
				Animate o\EntityX,1,0,1,10
				o\AlwaysPresent=False
				If o\Hit and p\Objects\Position\y#>o\Position\y# Then
					o\Mode=0.25*secs#
					o\State=1
					o\AlwaysPresent=True
				EndIf
			Case 1:
				If Not(o\Mode>0) Then
					EmitSmartSound(Sound_Crumble,o\Entity)
					Animate o\EntityX,3,0.5,1,10
					o\Mode=0.875*secs#
					o\State=2
				EndIf
			Case 2:
				If Not(o\Mode>0) Then
					EmitSmartSound(Sound_Crusher,o\Entity)
					o\Mode=3*secs#
					o\State=3
				EndIf
			Case 3:
				MoveEntity o\Entity, 0, -2*d\Delta, 0
				If Not(o\Mode>0) Then
					o\Mode=3.5*secs#
					o\State=4
				EndIf
			Case 4:
				If Not(o\Mode>0) Then
					Objects_Reset_Object(o)
				EndIf
		End Select

	End Function

; /\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/
; /\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/

	Function Object_Handle_Create.tObject(x#, y#, z#, yaw#)
		o.tObject = New tObject : o\ObjType = TempAttribute\ObjectNo : o\ID=TempAttribute\ObjectID

		Object_CreateHitBox(HITBOXTYPE_SPEEDY,o,5.875,5.25,5.875)

		Object_Acquire_Position(o,x#,y#,z#)
		Object_Acquire_Rotation(o,0,yaw#,0)

		o\Entity = CopyEntity(MESHES(SmartEntity(Mesh_Handle)), Game\Stage\Root)

		Return o
	End Function
	
	; =========================================================================================================

	Function Object_Handle_Update(o.tObject, p.tPlayer)

		; Player collided with object
		If o\Hit Then
			; If player is already holding
			If p\Action=ACTION_HOLD Then
				; Player's speed and direction
				Player_SetSpeed(p,0) : Player_SetSpeedY(p,0)
				p\Animation\Direction#=o\Rotation\y#
				PositionEntity p\Objects\Entity, EntityX(o\Entity), EntityY(o\Entity), EntityZ(o\Entity), 1
				MoveEntity p\Objects\Entity, 0, -(0.5+p\ScaleFactor#), 0
				Player_FollowerHolding_ByFeet(p,true)
				Player_Motion_ResetAirBeg(p)
				p\ShouldBeHoldingTimer=0.25*secs#

			; If player is not holding yet
			ElseIf (Not(p\ShouldBeHoldingTimer>0)) and p\Objects\Position\y#<o\Position\y#+1 Then
				p\Action=ACTION_HOLD
				Game\Vehicle=0
				p\IceFloorTimer=0
				p\Flags\HomingWasLockedTimer=0
				Player_SetSpeed(p,0) : Player_SetSpeedY(p,0)
				PositionEntity p\Objects\Entity, EntityX(o\Entity), EntityY(o\Entity), EntityZ(o\Entity), 1
				p\ShouldBeHoldingTimer=0.1*secs# : p\IsHoldingTimer=0.5*secs#
				EmitSmartSound(Sound_Grab,o\Entity)
				p\JustGrabbedPulleyTimer=0.1875*secs#
			EndIf
		EndIf
		
	End Function