from pathlib import Path
from PIL import Image, ImageDraw, ImageFont


WIDTH = 2200
HEIGHT = 1400
BG = "#fcfcff"
BOX_FILL = "#eef0ff"
BOX_BORDER = "#6468a6"
TEXT = "#1f2142"
LINE = "#555770"
MUTED = "#7a7ca0"


def load_font(size: int, bold: bool = False):
    candidates = [
        "C:/Windows/Fonts/arialbd.ttf" if bold else "C:/Windows/Fonts/arial.ttf",
        "C:/Windows/Fonts/segoeuib.ttf" if bold else "C:/Windows/Fonts/segoeui.ttf",
    ]
    for candidate in candidates:
        try:
            return ImageFont.truetype(candidate, size)
        except OSError:
            continue
    return ImageFont.load_default()


TITLE_FONT = load_font(34, bold=True)
CLASS_FONT = load_font(26, bold=True)
BODY_FONT = load_font(21)
SMALL_FONT = load_font(20)
LABEL_FONT = load_font(22, bold=True)


def draw_box(draw: ImageDraw.ImageDraw, x: int, y: int, w: int, h: int, title: str, lines, stereotype: str | None = None):
    draw.rounded_rectangle((x, y, x + w, y + h), radius=18, fill=BOX_FILL, outline=BOX_BORDER, width=3)
    header_bottom = y + 58
    draw.line((x, header_bottom, x + w, header_bottom), fill=BOX_BORDER, width=2)

    title_bbox = draw.textbbox((0, 0), title, font=CLASS_FONT)
    title_w = title_bbox[2] - title_bbox[0]
    draw.text((x + (w - title_w) / 2, y + 14), title, fill=TEXT, font=CLASS_FONT)

    current_y = header_bottom + 12
    if stereotype:
        draw.text((x + 18, current_y), stereotype, fill=MUTED, font=SMALL_FONT)
        current_y += 34

    for line in lines:
        draw.text((x + 18, current_y), line, fill=TEXT, font=BODY_FONT)
        current_y += 29


def draw_arrow(draw: ImageDraw.ImageDraw, start, end, dashed=False, hollow=False, diamond=False, label=None):
    sx, sy = start
    ex, ey = end

    if dashed:
        dx = ex - sx
        dy = ey - sy
        length = max((dx * dx + dy * dy) ** 0.5, 1)
        step = 18
        dash = 10
        pos = 0
        while pos < length:
            s = pos / length
            e = min(pos + dash, length) / length
            x1 = sx + dx * s
            y1 = sy + dy * s
            x2 = sx + dx * e
            y2 = sy + dy * e
            draw.line((x1, y1, x2, y2), fill=LINE, width=3)
            pos += step
    else:
        draw.line((sx, sy, ex, ey), fill=LINE, width=3)

    if diamond:
        size = 14
        points = [(ex, ey), (ex - size, ey - size), (ex - 2 * size, ey), (ex - size, ey + size)]
        draw.polygon(points, outline=LINE, fill=BG)
    else:
        size = 18
        if abs(ex - sx) >= abs(ey - sy):
            sign = 1 if ex >= sx else -1
            points = [(ex, ey), (ex - sign * size, ey - size / 2), (ex - sign * size, ey + size / 2)]
        else:
            sign = 1 if ey >= sy else -1
            points = [(ex, ey), (ex - size / 2, ey - sign * size), (ex + size / 2, ey - sign * size)]
        draw.polygon(points, outline=LINE, fill=BG if hollow else LINE)

    if label:
        mx = (sx + ex) / 2
        my = (sy + ey) / 2
        bbox = draw.textbbox((0, 0), label, font=SMALL_FONT)
        pad = 6
        draw.rounded_rectangle(
            (mx - (bbox[2] - bbox[0]) / 2 - pad, my - 18, mx + (bbox[2] - bbox[0]) / 2 + pad, my + 10),
            radius=8,
            fill=BG,
            outline=None,
        )
        draw.text((mx - (bbox[2] - bbox[0]) / 2, my - 14), label, fill=TEXT, font=SMALL_FONT)


image = Image.new("RGB", (WIDTH, HEIGHT), BG)
draw = ImageDraw.Draw(image)

title = "Diagrama de Classes M3-M7"
subtitle = "Atualizado com DispatchAction, decorators concretos, NoOpDispatchAction e uso em LogDispatcher"
title_bbox = draw.textbbox((0, 0), title, font=TITLE_FONT)
subtitle_bbox = draw.textbbox((0, 0), subtitle, font=BODY_FONT)
draw.text(((WIDTH - (title_bbox[2] - title_bbox[0])) / 2, 38), title, fill=TEXT, font=TITLE_FONT)
draw.text(((WIDTH - (subtitle_bbox[2] - subtitle_bbox[0])) / 2, 88), subtitle, fill=MUTED, font=BODY_FONT)

draw.text((170, 155), "decorators", fill=TEXT, font=LABEL_FONT)
draw.text((1470, 155), "service", fill=TEXT, font=LABEL_FONT)
draw.text((1540, 1040), "support", fill=TEXT, font=LABEL_FONT)

boxes = {
    "dispatch_action": (550, 180, 560, 165),
    "noop": (160, 460, 380, 120),
    "decorator": (540, 430, 590, 170),
    "admin": (120, 780, 400, 120),
    "monitoring": (590, 760, 460, 150),
    "error": (1120, 750, 460, 170),
    "dispatcher": (1500, 300, 560, 205),
    "bridge": (1660, 640, 260, 105),
    "log_entry": (1450, 1080, 300, 105),
    "log_destination": (1820, 1080, 320, 105),
}

draw_box(draw, *boxes["dispatch_action"], "DispatchAction", [
    "+ onFiltered(logEntry: LogEntry)",
    "+ onDispatched(logEntry: LogEntry, destination: LogDestination, formattedLog: String)",
], stereotype="<<interface>>")

draw_box(draw, *boxes["noop"], "NoOpDispatchAction", [], stereotype="implements DispatchAction")
draw_box(draw, *boxes["decorator"], "DispatchActionDecorator", [
    "# next: DispatchAction",
    "+ onFiltered(...)",
    "+ onDispatched(...)",
], stereotype="<<abstract>> implements DispatchAction")
draw_box(draw, *boxes["admin"], "AdminAlertDecorator", [], stereotype="extends DispatchActionDecorator")
draw_box(draw, *boxes["monitoring"], "MonitoringIntegrationDecorator", [
    "- counters: Map<String, AtomicInteger>",
], stereotype="extends DispatchActionDecorator")
draw_box(draw, *boxes["error"], "ErrorPatternAnalysisDecorator", [
    "- errorPatterns: Map<String, AtomicInteger>",
    "- threshold: int",
], stereotype="extends DispatchActionDecorator")

draw_box(draw, *boxes["dispatcher"], "LogDispatcher", [
    "- logBridge: LogBridge",
    "- dispatchAction: DispatchAction",
    "+ dispatch(logEntry: LogEntry)",
    "+ setDispatchAction(dispatchAction: DispatchAction)",
])
draw_box(draw, *boxes["bridge"], "LogBridge", [], stereotype="service")
draw_box(draw, *boxes["log_entry"], "LogEntry", [], stereotype="abstract")
draw_box(draw, *boxes["log_destination"], "LogDestination", [], stereotype="enum")


def center_top(box):
    x, y, w, _ = box
    return x + w / 2, y


def center_bottom(box):
    x, y, w, h = box
    return x + w / 2, y + h


def center_left(box):
    x, y, _, h = box
    return x, y + h / 2


def center_right(box):
    x, y, w, h = box
    return x + w, y + h / 2


draw_arrow(draw, center_top(boxes["noop"]), (center_top(boxes["noop"])[0], boxes["dispatch_action"][1] + boxes["dispatch_action"][3]), dashed=True, hollow=True)
draw_arrow(draw, center_top(boxes["decorator"]), (center_top(boxes["decorator"])[0], boxes["dispatch_action"][1] + boxes["dispatch_action"][3]), dashed=True, hollow=True)

draw_arrow(draw, center_top(boxes["admin"]), center_bottom(boxes["decorator"]), hollow=True)
draw_arrow(draw, center_top(boxes["monitoring"]), (boxes["decorator"][0] + boxes["decorator"][2] / 2, boxes["decorator"][1] + boxes["decorator"][3]), hollow=True)
draw_arrow(draw, center_top(boxes["error"]), (boxes["decorator"][0] + boxes["decorator"][2] / 2 + 140, boxes["decorator"][1] + boxes["decorator"][3]), hollow=True)

decorator_right = center_right(boxes["decorator"])
dispatch_left = center_left(boxes["dispatch_action"])
mid_x = 1250
mid_y1 = decorator_right[1]
mid_y2 = dispatch_left[1]
draw.line((decorator_right[0], decorator_right[1], mid_x, mid_y1), fill=LINE, width=3)
draw.line((mid_x, mid_y1, mid_x, mid_y2), fill=LINE, width=3)
draw_arrow(draw, (mid_x, mid_y2), dispatch_left, diamond=True, label="next")

draw_arrow(draw, center_left(boxes["dispatcher"]), center_right(boxes["dispatch_action"]), label="uses")
draw_arrow(draw, center_bottom(boxes["dispatcher"]), center_top(boxes["bridge"]), dashed=True, label="uses")

dispatcher_bottom = center_bottom(boxes["dispatcher"])
draw.line((dispatcher_bottom[0] - 80, dispatcher_bottom[1], dispatcher_bottom[0] - 80, 1130), fill=LINE, width=3)
draw_arrow(draw, (dispatcher_bottom[0] - 80, 1130), center_top(boxes["log_entry"]), dashed=True)
draw.line((dispatcher_bottom[0] + 90, dispatcher_bottom[1], dispatcher_bottom[0] + 90, 1130), fill=LINE, width=3)
draw_arrow(draw, (dispatcher_bottom[0] + 90, 1130), center_top(boxes["log_destination"]), dashed=True)

output = Path("Trabalho Prático/Sprint1/diagrama-classes-m3-m7.jpg")
image.save(output, quality=95)
print(output)
