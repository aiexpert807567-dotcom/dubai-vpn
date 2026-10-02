package com.dubaivpn.app.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.vector.PathParser
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dubaivpn.app.ui.theme.DvColors

/** Developer contact details. Change the number here, in one place. */
object Contact {
    const val DISPLAY_NUMBER = "+971 52 763 6270"
    const val WHATSAPP_URL =
        "https://wa.me/971527636270?text=Hi%2C%20I%20have%20a%20question%20about%20Dubai%20VPN"
}

private const val CALL_ICON_PATH =
    "M6.62,10.79c1.44,2.83 3.76,5.14 6.59,6.59l2.2,-2.2c0.27,-0.27 0.67,-0.36 1.02,-0.24 " +
    "1.12,0.37 2.33,0.57 3.57,0.57 0.55,0 1,0.45 1,1V20c0,0.55 -0.45,1 -1,1 -9.39,0 -17,-7.61 " +
    "-17,-17 0,-0.55 0.45,-1 1,-1h3.5c0.55,0 1,0.45 1,1 0,1.25 0.2,2.45 0.57,3.57 0.11,0.35 " +
    "0.03,0.74 -0.25,1.02l-2.2,2.2z"

/** Green chat-bubble-with-phone icon, drawn in code (no image files needed). */
@Composable
fun WhatsAppIcon(modifier: Modifier = Modifier) {
    val handset = remember {
        runCatching { PathParser().parsePathString(CALL_ICON_PATH).toPath() }.getOrNull()
    }
    Canvas(modifier) {
        val s = size.minDimension
        drawCircle(DvColors.WhatsApp)

        val center = Offset(s * 0.5f, s * 0.47f)
        drawCircle(
            color = Color.White,
            radius = s * 0.29f,
            center = center,
            style = Stroke(width = s * 0.06f)
        )
        val tail = Path().apply {
            moveTo(s * 0.24f, s * 0.80f)
            lineTo(s * 0.29f, s * 0.60f)
            lineTo(s * 0.44f, s * 0.72f)
            close()
        }
        drawPath(tail, Color.White)

        if (handset != null) {
            val k = (s * 0.30f) / 24f
            withTransform({
                translate(left = center.x - 12f * k, top = center.y - 12f * k)
                scale(k, k, pivot = Offset.Zero)
            }) {
                drawPath(handset, Color.White)
            }
        }
    }
}

/** Floating "Contact developer" pill: gently bobbing icon with a soft pulsing ring. */
@Composable
fun ContactDeveloperButton(onClick: () -> Unit) {
    val transition = rememberInfiniteTransition(label = "whatsapp-motion")
    val pulse by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(1800, easing = LinearEasing), RepeatMode.Restart),
        label = "pulse"
    )
    val bob by transition.animateFloat(
        initialValue = 1f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(tween(900, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "bob"
    )
    val shape = RoundedCornerShape(50)

    Row(
        modifier = Modifier
            .shadow(6.dp, shape)
            .clip(shape)
            .background(DvColors.TextPrimary)
            .clickable(role = Role.Button, onClick = onClick)
            .padding(start = 6.dp, end = 18.dp, top = 6.dp, bottom = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.size(52.dp), contentAlignment = Alignment.Center) {
            Canvas(modifier = Modifier.size(52.dp)) {
                drawCircle(
                    color = DvColors.WhatsApp.copy(alpha = 0.40f * (1f - pulse)),
                    radius = size.minDimension / 2f * (0.85f + 0.15f * pulse)
                )
            }
            WhatsAppIcon(modifier = Modifier.size(44.dp).scale(bob))
        }
        Spacer(Modifier.width(10.dp))
        Text(
            text = "Contact developer",
            color = DvColors.OnAccent,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun ContactDeveloperDialog(onDismiss: () -> Unit) {
    val uriHandler = LocalUriHandler.current
    val numberLink = buildAnnotatedString {
        withLink(
            LinkAnnotation.Url(
                Contact.WHATSAPP_URL,
                TextLinkStyles(
                    style = SpanStyle(
                        color = DvColors.Link,
                        textDecoration = TextDecoration.Underline,
                        fontWeight = FontWeight.SemiBold
                    )
                )
            )
        ) {
            append(Contact.DISPLAY_NUMBER)
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = DvColors.Card,
        titleContentColor = DvColors.TextPrimary,
        textContentColor = DvColors.TextMuted,
        title = { Text("Contact developer") },
        text = {
            Column {
                Text("Questions, bugs or ideas? Message me on WhatsApp:")
                Spacer(Modifier.height(12.dp))
                Text(text = numberLink, fontSize = 20.sp)
            }
        },
        confirmButton = {
            TextButton(onClick = {
                uriHandler.openUri(Contact.WHATSAPP_URL)
                onDismiss()
            }) {
                Text("Open WhatsApp", color = DvColors.Link)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Close", color = DvColors.TextMuted)
            }
        }
    )
}
