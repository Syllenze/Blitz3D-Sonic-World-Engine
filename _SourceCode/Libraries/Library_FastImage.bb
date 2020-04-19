Const FI_VERSION$ = "1.74.9"

;CreateImageEx Flags
Const FI_AUTOFLAGS = -1
Const FI_NONE = 0
Const FI_MIDHANDLE = 1
Const FI_FILTEREDIMAGE = 2
Const FI_FILTERED = 2

;SetBlend Flags
Const FI_SOLIDBLEND = 0
Const FI_ALPHABLEND = 1
Const FI_LIGHTBLEND = 2
Const FI_SHADEBLEND = 3
Const FI_MASKBLEND = 4
Const FI_MASKBLEND2 = 5
Const FI_INVALPHABLEND = 6

;ImageFonts Flags
Const FI_SMOOTHFONT=1

;DrawImagePart Wrap Flags
Const FI_NOWRAP = 0
Const FI_WRAPU = 1
Const FI_MIRRORU = 2
Const FI_WRAPV = 4
Const FI_MIRRORV = 8
Const FI_WRAPUV = 5
Const FI_MIRRORUV = 10

;DrawPoly consts
Const FI_POINTLIST     = 1
Const FI_LINELIST      = 2
Const FI_LINESTRIP     = 3
Const FI_TRIANGLELIST  = 4
Const FI_TRIANGLESTRIP = 5
Const FI_TRIANGLEFAN   = 6
Const FI_COLOROVERLAY = 1

Type FI_PropertyType
	Field Blend%
	Field Alpha#, Red%, Green%, Blue%
	Field ColorVertex0%, ColorVertex1%, ColorVertex2%, ColorVertex3%                                 	
	Field Rotation#, ScaleX#, ScaleY#
	Field MatrixXX#, MatrixXY#, MatrixYX#, MatrixYY#
	Field HandleX#, HandleY#
	Field OriginX#, OriginY#
	Field AutoHandle%, AutoFlags%
	Field LineWidth#
	Field ViewportX%, ViewportY%, ViewportWidth%, ViewportHeight%
	Field MipLevel%
	Field ProjScaleX#, ProjScaleY#, ProjRotation#
	Field ProjOriginX#, ProjOriginY#
	Field ProjHandleX#, ProjHandleY#
	Field CoordsRound%
	Field TintLevel#, TintColorRed%, TintColorGreen%, TintColorBlue%
	Field Reserved0%
	Field Reserved1%
End Type

Global FI_Property.FI_PropertyType = New FI_PropertyType

Type FI_ImagePropertyType
	Field HandleX#
	Field HandleY#
	Field Width%
	Field Height%
	Field Frames%
	Field Flags%
	Field Texture%
	Field TextureWidth%
	Field TextureHeight%
	Field TextureFlags%
	Field TextureFrames%
	Field TextureUpdate%
	Field Reserved0%
	Field Reserved1%
End Type

Global FI_ImageProperty.FI_ImagePropertyType = New FI_ImagePropertyType




Type FI_FontPropertyType
	; public
	Field Width%
	Field Height%
	Field FirstChar%
	Field Kerning%
	Field KerningCustom%
	Field Image%
	Field FrameWidth%
	Field FrameHeight%
	Field FrameCount%
	Field FrameX%
	Field FrameY%
	Field Chars[256]
	; system / protected
	Field TextureFlags%
	Field ImageFlags%
	Field TexturePath$
	Field Reserved0%
	Field Reserved1%
End Type
Global FI_FontProperty.FI_FontPropertyType = New FI_FontPropertyType

Type FI_TestType
	Field Result%
	Field ProjectedX#, ProjectedY#
	Field RectX#, RectY#
	Field RectU#, RectV#
	Field TextureX%, TextureY%
	Field Texture%
	Field Frame%
	Field Reserved0%
	Field Reserved1%
End Type
Global FI_Test.FI_TestType = New FI_TestType

Function SetCustomBlend(src%, dest%)
	SetCustomState 15,0			;DX7  SetRenderState ( D3DRENDERSTATE_AlphaTestEnable, False )
	SetCustomState 27,1			;DX7  SetRenderState ( D3DRENDERSTATE_AlphaBlendEnable, True )
	SetCustomState 19,src			;DX7  SetRenderState ( D3DRENDERSTATE_SrcBlend, src )
	SetCustomState 20,dest			;DX7  SetRenderState ( D3DRENDERSTATE_DestBlend, dest )
End Function

Function CreateImageEx% (texure%, width%, height%, imageFlags%=FI_AUTOFLAGS)
	Return CreateImageEx_(texure, width, height, imageFlags)
End Function

Function LoadImageEx% (fileName$, textureFlags%=0, imageFlags%=FI_AUTOFLAGS)
	Return LoadImageEx_( fileName, LoadTexture (fileName, textureFlags), imageFlags)
End Function

Function LoadAnimImageEx% ( fileName$, textureFlags%, frameWidth%, frameHeight%, firstFrame%, frameCount%, imageFlags%=FI_AUTOFLAGS )
	textureFlags = (textureFlags And $3F) Or $9
	Return CreateImageEx_( LoadAnimTexture (fileName, textureFlags, frameWidth, frameHeight, firstFrame, frameCount), frameWidth, frameHeight, imageFlags)
End Function

Function DrawImageEx% (image%, x#, y#, frame%=0)
	Return DrawImageEx_(image, x, y, frame)
End Function

Function DrawImageRectEx% (image%, x#, y#, width#, height#, frame%=0)
	Return DrawImageRectEx_(image, x, y, width, height, frame)
End Function

Function DrawImagePart% (image%, x#, y#, width#, height#, partX#=0, partY#=0, partWidth#=0, partHeight#=0, frame%=0, wrap%=FI_NOWRAP)
	Return DrawImagePart_(image, x, y, width, height, partX, partY, partWidth, partHeight, frame, wrap)
End Function

Function DrawPoly% (x#, y#, bank%, image%=0, frame%=0, color%=FI_NONE)
	Return DrawPoly_(x, y, bank, image, frame, color)
End Function

Function DrawPolyEx% (x#, y#, bank%, image%=0, frame%=0, color%=FI_NONE)
	Return DrawPolyEx_(x, y, bank, image, frame, color)
End Function

Function DrawRect% (x#, y#, width#, height#, fill%=1)
	DrawRect_ x, y, width, height, fill
End Function

Function DrawRectSimple% (x#, y#, width#, height#, fill%=1)
	DrawRectSimple_ x, y, width, height, fill
End Function

Function LoadImageFont% (filename$, flags%=FI_SMOOTHFONT)
	FI_FontProperty\TexturePath = LoadFontProperty_ ( FI_FontProperty, filename, flags )
	If Len(FI_FontProperty\TexturePath)>0 Then 	
		FI_FontProperty\Image = LoadImageEx ( FI_FontProperty\TexturePath, FI_FontProperty\TextureFlags, FI_FontProperty\ImageFlags )
		Return CreateImageFont ( FI_FontProperty )	
	Else
		Return 0
	EndIf
End Function

Function SetImageFont% (font%, customKerning%=-9999)
	Return SetImageFont_ (font, customKerning)
End Function

Function StringWidthEx% (txt$, maxWidth%=10000)
	Return StringWidthEx_(txt, maxWidth)
End Function

Function DrawText% (txt$, x#, y#, centerX%=0, centerY%=0, maxWidth%=10000)
	Return DrawText_(txt, x, y, centerX, centerY, maxWidth)
End Function

Function DrawTextRect% (txt$, x#, y#, w#, h#, centerX%=0, centerY%=0, lineSpacing%=0)
	Return DrawTextRect_(txt, x, y, w, h, centerX, centerY, lineSpacing)
End Function

Function DrawOutlineText(txt$="", x, y,  center_x = False, center_y = False)
	r = ColorRed()
	g = ColorGreen()
	b = ColorBlue()
	SetColor 0, 0, 0
	DrawText(txt$,x+1, y, center_x, center_y)
	DrawText(txt$,x-1, y, center_x, center_y)
	DrawText(txt$,x, y+1, center_x, center_y)
	DrawText(txt$,x, y-1, center_x, center_y)
	SetColor r, g, b
	DrawText(txt$,x, y, center_x, center_y)
End Function

Function InitDraw% (def=0)
	If FI_VERSION<>DrawVersion() Then
		RuntimeError "ERROR! FastImage library - Incorrect versions for FastImage.dll (v"+DrawVersion()+") and FastImage.bb (v"+FI_VERSION+")"
	Else
		DebugLog "Init FastImage library v"+FI_VERSION+" successful"
	EndIf
	Return InitDraw_ ( SystemProperty("Direct3DDevice7"), SystemProperty("DirectDraw7") )
End Function

Function GetProperty% ()
	Return GetProperty_ (FI_Property)
End Function

Function GetImageProperty% (image%)
	Return GetImageProperty_ (image, FI_ImageProperty)
End Function

function GetImageX#(Image%)
	GetImageProperty(Image%)
	Return FI_ImageProperty\HandleX#
end function
function GetImageY#(Image%)
	GetImageProperty(Image%)
	Return FI_ImageProperty\HandleY#
end function
function ImageWidthEx#(Image%)
	GetImageProperty(Image%)
	Return FI_ImageProperty\Width#
end function
function ImageHeightEx#(Image%)
	GetImageProperty(Image%)
	Return FI_ImageProperty\Height#
end function

Function GetFontProperty% (font%)
	Return GetFontProperty_ (font, FI_FontProperty)
End Function

Function TestRect% (xPoint#, yPoint#, xRect#, yRect#, WidthRect#, HeightRect#, Loc%=0)
	Return TestRect_ (xPoint, yPoint, xRect, yRect, WidthRect, HeightRect, Loc, FI_Test, 1)
End Function

Function TestOval% (xPoint#, yPoint#, xOval#, yOval#, WidthOval#, HeightOval#, Loc%=0)
	Return TestOval_ (xPoint, yPoint, xOval, yOval, WidthOval, HeightOval, Loc, FI_Test, 1)
End Function

Function TestImage% (xPoint#, yPoint#, xImage#, yImage#, Image%, alphaLevel%=0, Frame%=0, Loc%=0)
	If TestImage_ (xPoint, yPoint, xImage, yImage, Image, Loc, FI_Test, 1) And alphaLevel>0 And FI_Test\Texture<>0 Then
		If ( ReadPixel( FI_Test\TextureX, FI_Test\TextureY, TextureBuffer(FI_Test\Texture,Frame) ) Shr 24 ) < alphaLevel Then FI_Test\Result = 0
	EndIf
	Return FI_Test\Result
End Function

Function TestRendered% (xPoint#, yPoint#, alphaLevel%=0, Loc%=0)
	If TestRendered_ (xPoint, yPoint, Loc, FI_Test, 1) And alphaLevel>0 And FI_Test\Texture<>0 Then
		If ( ReadPixel( FI_Test\TextureX, FI_Test\TextureY, TextureBuffer(FI_Test\Texture,FI_Test\Frame) ) Shr 24 ) < alphaLevel Then FI_Test\Result = 0
	EndIf
	Return FI_Test\Result
End Function

Function FreeImageEx% (image%, FreeTexture%=0)
	If FreeTexture<>0 And GetImageProperty(image)<>0 And FI_ImageProperty\Texture<>0 Then FreeTexture FI_ImageProperty\Texture
	FreeImageEx_ image
End Function

Function FreeImageFont% (font%)
	If GetFontProperty(font)<>0 And FI_FontProperty\Image<>0 Then
		If GetImageProperty(FI_FontProperty\Image)<>0 And FI_ImageProperty\Texture<>0 Then FreeTexture FI_ImageProperty\Texture
	EndIf
	FreeImageFont_ font
End Function

Function UpdateImage% (image%, texture%, width%=0, height%=0)
	Return UpdateImage_ (image, texture, width, height)
End Function

Function UpdateImageFlag% (image%, update%=-1)
	Return UpdateImageFlag_ (image, update)
End Function

Function SetDefault% (all%=1)
	Return SetDefaultProperty_(all) 
End Function