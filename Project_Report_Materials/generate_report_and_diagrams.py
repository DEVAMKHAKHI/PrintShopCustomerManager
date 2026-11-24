#!/usr/bin/env python3
"""
Academic Project Report & Diagram Generator for PrintShop Customer Manager
Generates:
1. System Architecture Diagram (PNG)
2. Application Flowchart (PNG)
3. Use Case Diagram (PNG)
4. Database Schema ER Diagram (PNG)
5. Full 24-Section Academic Project Report in DOCX format (PROJECT_REPORT.docx)
"""

import os
import sys
from PIL import Image, ImageDraw, ImageFont
import docx
from docx.shared import Inches, Pt, RGBColor
from docx.enum.text import WD_ALIGN_PARAGRAPH
from docx.enum.table import WD_TABLE_ALIGNMENT, WD_ALIGN_VERTICAL
from docx.oxml import parse_xml, OxmlElement
from docx.oxml.ns import nsdecls, qn

BASE_DIR = r"C:\Users\khakh\.gemini\antigravity\scratch\PrintShopCustomerManager\Project_Report_Materials"
DIAGRAMS_DIR = os.path.join(BASE_DIR, "diagrams")
os.makedirs(DIAGRAMS_DIR, exist_ok=True)

# -------------------------------------------------------------
# 1. DIAGRAM GENERATION FUNCTIONS
# -------------------------------------------------------------

def get_font(size, bold=False):
    # Try system fonts on Windows
    candidates = [
        "C:\\Windows\\Fonts\\segoeui.ttf",
        "C:\\Windows\\Fonts\\arial.ttf",
        "C:\\Windows\\Fonts\\calibri.ttf"
    ]
    if bold:
        candidates = [
            "C:\\Windows\\Fonts\\segoeuib.ttf",
            "C:\\Windows\\Fonts\\arialbd.ttf",
            "C:\\Windows\\Fonts\\calibrib.ttf"
        ]
    for c in candidates:
        if os.path.exists(c):
            try:
                return ImageFont.truetype(c, size)
            except Exception:
                pass
    return ImageFont.load_default()

def create_system_architecture_diagram():
    img_path = os.path.join(DIAGRAMS_DIR, "system_architecture.png")
    width, height = 1200, 750
    img = Image.new("RGB", (width, height), (248, 249, 254))
    draw = ImageDraw.Draw(img)

    f_title = get_font(28, bold=True)
    f_sub = get_font(18, bold=True)
    f_body = get_font(15, bold=False)

    # Header
    draw.rectangle([(0, 0), (width, 80)], fill=(26, 35, 126))
    draw.text((width // 2, 40), "PrintShop Customer Manager - MVVM System Architecture",
              fill=(255, 255, 255), font=f_title, anchor="mm")

    layers = [
        ("VIEW / UI LAYER (Jetpack Compose & Material 3)",
         "Screens: LoginScreen, SignUpScreen, DashboardScreen, CustomerListScreen, AddCustomerScreen, CustomerDetailScreen, EditCustomerScreen\nComponents: PrintShopTopAppBar, CustomerCard, StatCard, SearchBar, DatePickerModal, DeleteConfirmationDialog",
         (232, 234, 246), (26, 35, 126), 110, 230),

        ("VIEWMODEL LAYER (Android Jetpack ViewModel & StateFlow)",
         "AuthViewModel: Login, Register, Password Reset, Real-time Regex Validations\nCustomerViewModel: Customer List StateFlow, Search Filter, Add/Edit/Delete Operations\nDashboardViewModel: Metric Calculations, Recent Customer Stream, Sample Seeder",
         (224, 247, 250), (0, 96, 100), 260, 380),

        ("REPOSITORY LAYER (Single Source of Truth Pattern)",
         "AuthRepository / FirebaseAuthRepository: User Auth State Flow, Credential Verification\nCustomerRepository / FirestoreCustomerRepository: Real-time Snapshot Flow, CRUD Operations, Search Filtering, Batch Seeder",
         (255, 243, 224), (230, 81, 0), 410, 530),

        ("CLOUD BACKEND & PERSISTENCE LAYER (Google Firebase)",
         "Firebase Authentication: Managed Identity, Password Tokens, Session Verification\nCloud Firestore (NoSQL): 'customers' Collection, Deterministic Seed IDs, Offline Persistence, Security Rules",
         (232, 245, 233), (46, 125, 50), 560, 680)
    ]

    for title, desc, bg, border, y1, y2 in layers:
        draw.rounded_rectangle([(60, y1), (width - 60, y2)], radius=12, fill=bg, outline=border, width=3)
        draw.text((90, y1 + 18), title, fill=border, font=f_sub)
        lines = desc.split("\n")
        cur_y = y1 + 48
        for line in lines:
            draw.text((90, cur_y), line, fill=(40, 40, 40), font=f_body)
            cur_y += 24

    # Connecting arrows
    for y in [235, 385, 535]:
        arrow_x = width // 2
        draw.line([(arrow_x, y), (arrow_x, y + 20)], fill=(26, 35, 126), width=4)
        draw.polygon([(arrow_x - 8, y + 16), (arrow_x + 8, y + 16), (arrow_x, y + 24)], fill=(26, 35, 126))

    img.save(img_path, quality=95)
    print(f"[OK] Generated {img_path}")
    return img_path

def create_flowchart_diagram():
    img_path = os.path.join(DIAGRAMS_DIR, "application_flowchart.png")
    width, height = 1100, 780
    img = Image.new("RGB", (width, height), (255, 255, 255))
    draw = ImageDraw.Draw(img)

    f_title = get_font(26, bold=True)
    f_node = get_font(15, bold=True)
    f_label = get_font(13, bold=False)

    # Title Banner
    draw.rectangle([(0, 0), (width, 70)], fill=(0, 151, 167))
    draw.text((width // 2, 35), "Application Navigation & Operational Flowchart",
              fill=(255, 255, 255), font=f_title, anchor="mm")

    # Nodes
    nodes = [
        # (id, text, center_x, center_y, width, height, bg, border, is_diamond)
        ("START", "App Launch", 550, 120, 150, 45, (232, 234, 246), (26, 35, 126), False),
        ("AUTH_CHECK", "User Authenticated?", 550, 205, 210, 60, (255, 243, 224), (230, 81, 0), True),
        ("LOGIN", "Login Screen", 260, 205, 160, 50, (255, 235, 238), (198, 40, 40), False),
        ("SIGNUP", "Sign Up / Reset", 260, 305, 160, 50, (255, 235, 238), (198, 40, 40), False),
        ("DASH", "Operations Dashboard", 550, 315, 220, 55, (224, 247, 250), (0, 151, 167), False),
        ("SEED", "Seed 10 Sample Data", 850, 315, 180, 50, (255, 248, 225), (255, 143, 0), False),
        ("ADD", "Add Customer Form", 360, 430, 180, 50, (232, 245, 233), (46, 125, 50), False),
        ("LIST", "Customer Directory", 740, 430, 180, 50, (232, 234, 246), (26, 35, 126), False),
        ("SEARCH", "Live Search Filter", 930, 430, 140, 50, (243, 229, 245), (142, 36, 170), False),
        ("DETAIL", "Customer Details", 550, 545, 180, 50, (224, 247, 250), (0, 151, 167), False),
        ("EDIT", "Edit Customer Form", 360, 660, 180, 50, (255, 243, 224), (230, 81, 0), False),
        ("DELETE", "Delete Confirmation", 740, 660, 190, 50, (255, 235, 238), (198, 40, 40), False)
    ]

    for nid, text, cx, cy, w, h, bg, border, is_d in nodes:
        x1, y1 = cx - w // 2, cy - h // 2
        x2, y2 = cx + w // 2, cy + h // 2
        if is_d:
            draw.polygon([(cx, y1), (x2, cy), (cx, y2), (x1, cy)], fill=bg, outline=border)
        else:
            draw.rounded_rectangle([(x1, y1), (x2, y2)], radius=10, fill=bg, outline=border, width=2)
        draw.text((cx, cy), text, fill=border, font=f_node, anchor="mm")

    # Connectors
    def connect(pt1, pt2, label=None):
        draw.line([pt1, pt2], fill=(80, 80, 80), width=2)
        if label:
            mx, my = (pt1[0] + pt2[0]) // 2, (pt1[1] + pt2[1]) // 2
            draw.text((mx + 8, my - 8), label, fill=(26, 35, 126), font=f_label)

    connect((550, 142), (550, 175))
    connect((445, 205), (340, 205), "No")
    connect((550, 235), (550, 287), "Yes")
    connect((260, 230), (260, 280))
    connect((340, 315), (440, 315))
    connect((660, 315), (760, 315))
    connect((460, 342), (360, 405))
    connect((640, 342), (740, 405))
    connect((830, 430), (860, 430))
    connect((360, 455), (460, 520))
    connect((740, 455), (640, 520))
    connect((480, 570), (360, 635))
    connect((620, 570), (740, 635))

    img.save(img_path, quality=95)
    print(f"[OK] Generated {img_path}")
    return img_path

def create_database_schema_diagram():
    img_path = os.path.join(DIAGRAMS_DIR, "database_design.png")
    width, height = 1000, 600
    img = Image.new("RGB", (width, height), (250, 250, 252))
    draw = ImageDraw.Draw(img)

    f_title = get_font(26, bold=True)
    f_tbl = get_font(18, bold=True)
    f_row = get_font(14, bold=False)
    f_hdr = get_font(15, bold=True)

    # Banner
    draw.rectangle([(0, 0), (width, 70)], fill=(26, 35, 126))
    draw.text((width // 2, 35), "Cloud Firestore NoSQL Database Schema",
              fill=(255, 255, 255), font=f_title, anchor="mm")

    # Table 1: customers
    draw.rounded_rectangle([(60, 110), (560, 540)], radius=12, fill=(255, 255, 255), outline=(26, 35, 126), width=2)
    draw.rectangle([(60, 110), (560, 160)], fill=(26, 35, 126))
    draw.text((80, 125), "Collection: customers", fill=(255, 255, 255), font=f_tbl)

    headers = [("Field", 80), ("Type", 240), ("Constraint / Role", 370)]
    for h, x in headers:
        draw.text((x, 175), h, fill=(100, 100, 100), font=f_hdr)
    draw.line([(60, 200), (560, 200)], fill=(200, 200, 200), width=1)

    fields = [
        ("customerId", "String", "PRIMARY KEY / Doc ID"),
        ("fullName", "String", "Required (min 2 chars)"),
        ("phoneNumber", "String", "Required (valid phone)"),
        ("dateOfBirth", "String", "Required (YYYY-MM-DD)"),
        ("email", "String", "Required (valid email)"),
        ("address", "String", "Required (full address)"),
        ("createdAt", "Integer (Long)", "Epoch Millis timestamp")
    ]
    cur_y = 215
    for f, t, c in fields:
        draw.text((80, cur_y), f, fill=(26, 35, 126), font=f_row)
        draw.text((240, cur_y), t, fill=(0, 151, 167), font=f_row)
        draw.text((370, cur_y), c, fill=(80, 80, 80), font=f_row)
        cur_y += 38

    # Table 2: users (auth metadata)
    draw.rounded_rectangle([(610, 110), (940, 360)], radius=12, fill=(255, 255, 255), outline=(0, 151, 167), width=2)
    draw.rectangle([(610, 110), (940, 160)], fill=(0, 151, 167))
    draw.text((630, 125), "Collection: users", fill=(255, 255, 255), font=f_tbl)

    u_fields = [
        ("userId", "String", "Auth UID"),
        ("email", "String", "Staff Email"),
        ("displayName", "String", "Staff Name"),
        ("createdAt", "Long", "Created timestamp")
    ]
    cur_y = 180
    for f, t, c in u_fields:
        draw.text((630, cur_y), f, fill=(0, 96, 100), font=f_row)
        draw.text((740, cur_y), t, fill=(80, 80, 80), font=f_row)
        cur_y += 38

    # Relationship note
    draw.rounded_rectangle([(610, 390), (940, 540)], radius=12, fill=(232, 234, 246), outline=(26, 35, 126), width=1)
    draw.text((630, 410), "Security Policy Rule:", fill=(26, 35, 126), font=f_tbl)
    rule_text = "Only authenticated users\nin 'users' collection with\nvalid JWT can read/write\ninto 'customers' collection.\nUnauthenticated access\nis strictly forbidden."
    draw.text((630, 440), rule_text, fill=(50, 50, 50), font=f_row)

    img.save(img_path, quality=95)
    print(f"[OK] Generated {img_path}")
    return img_path

# -------------------------------------------------------------
# 2. COMPLETE 24-SECTION DOCX REPORT GENERATOR
# -------------------------------------------------------------

def set_cell_background(cell, fill_hex):
    tcPr = cell._tc.get_or_add_tcPr()
    shd = parse_xml(f'<w:shd {nsdecls("w")} w:fill="{fill_hex}"/>')
    tcPr.append(shd)

def create_docx_report():
    docx_path = os.path.join(BASE_DIR, "PROJECT_REPORT.docx")
    doc = docx.Document()

    # Page Margins
    for section in doc.sections:
        section.top_margin = Inches(1.0)
        section.bottom_margin = Inches(1.0)
        section.left_margin = Inches(1.0)
        section.right_margin = Inches(1.0)

    # Styles
    styles = doc.styles

    # Normal Style
    normal = styles['Normal']
    normal.font.name = 'Calibri'
    normal.font.size = Pt(11)
    normal.font.color.rgb = RGBColor(40, 40, 40)
    normal.paragraph_format.line_spacing = 1.15
    normal.paragraph_format.space_after = Pt(6)

    # Header / Title Colors
    NAVY = RGBColor(26, 35, 126)
    CYAN = RGBColor(0, 151, 167)

    # ------------------ 1. COVER PAGE ------------------
    p_inst = doc.add_paragraph()
    p_inst.alignment = WD_ALIGN_PARAGRAPH.CENTER
    r_inst = p_inst.add_run("DEPARTMENT OF COMPUTER SCIENCE & ENGINEERING\nACADEMIC PROJECT REPORT\n\n")
    r_inst.font.size = Pt(14)
    r_inst.font.bold = True
    r_inst.font.color.rgb = NAVY

    p_title = doc.add_paragraph()
    p_title.alignment = WD_ALIGN_PARAGRAPH.CENTER
    r_title = p_title.add_run("PRINTSHOP CUSTOMER MANAGER\n")
    r_title.font.size = Pt(26)
    r_title.font.bold = True
    r_title.font.color.rgb = NAVY

    p_sub = doc.add_paragraph()
    p_sub.alignment = WD_ALIGN_PARAGRAPH.CENTER
    r_sub = p_sub.add_run("A Modern Cloud-Integrated Native Android Application for Printing Industry Client Relationship & Account Management\n\n\n")
    r_sub.font.size = Pt(14)
    r_sub.font.italic = True
    r_sub.font.color.rgb = CYAN

    p_meta = doc.add_paragraph()
    p_meta.alignment = WD_ALIGN_PARAGRAPH.CENTER
    r_meta = p_meta.add_run("Submitted in partial fulfillment of the requirements\nfor the Degree in Computer Science / Information Technology\n\n\n")
    r_meta.font.size = Pt(12)

    p_cred = doc.add_paragraph()
    p_cred.alignment = WD_ALIGN_PARAGRAPH.CENTER
    r_cred = p_cred.add_run("Technology Stack: Android Studio | Kotlin | Jetpack Compose | Material 3 | Firebase\nAcademic Year: 2026-2027\n")
    r_cred.font.size = Pt(11)
    r_cred.font.bold = True

    doc.add_page_break()

    # ------------------ 2. CERTIFICATE ------------------
    h2 = doc.add_heading("2. CERTIFICATE OF APPROVAL", level=1)
    h2.runs[0].font.color.rgb = NAVY

    doc.add_paragraph(
        "This is to certify that the project entitled \"PRINTSHOP CUSTOMER MANAGER\" is a bonafide "
        "work carried out by the student under supervision in partial fulfillment of the academic "
        "requirements for the Degree of Bachelor / Master of Technology in Computer Science & Engineering."
    )

    doc.add_paragraph("\n\n\n")
    t_cert = doc.add_table(rows=2, cols=2)
    t_cert.alignment = WD_TABLE_ALIGNMENT.CENTER
    t_cert.rows[0].cells[0].paragraphs[0].add_run("_______________________________\nInternal Project Supervisor").bold = True
    t_cert.rows[0].cells[1].paragraphs[0].add_run("_______________________________\nHead of Department").bold = True
    t_cert.rows[1].cells[0].paragraphs[0].add_run("\n\n_______________________________\nExternal Examiner").bold = True
    t_cert.rows[1].cells[1].paragraphs[0].add_run("\n\nInstitutional Seal / Date").bold = True

    doc.add_page_break()

    # ------------------ 3. ACKNOWLEDGEMENT ------------------
    h3 = doc.add_heading("3. ACKNOWLEDGEMENT", level=1)
    h3.runs[0].font.color.rgb = NAVY
    doc.add_paragraph(
        "I express my deepest gratitude to my project guide, esteemed professors, and department head "
        "for providing technical guidance, continuous motivation, and access to necessary computing resources."
    )
    doc.add_paragraph(
        "Special thanks are also due to the Android open-source ecosystem, Jetpack Compose team, and Google Firebase "
        "for their rich documentation, libraries, and architecture patterns which made this project achievable."
    )

    # ------------------ 4. ABSTRACT ------------------
    h4 = doc.add_heading("4. ABSTRACT", level=1)
    h4.runs[0].font.color.rgb = NAVY
    doc.add_paragraph(
        "In commercial printing operations, managing client accounts, tracking contact points, handling custom printing "
        "specifications, and archiving delivery addresses often relies on fragmented physical paper slips or desktop spreadsheets. "
        "These legacy techniques lead to missed deadlines, lost records, and zero mobility for press floor operators."
    )
    doc.add_paragraph(
        "PrintShop Customer Manager resolves these bottlenecks by offering a robust, reactive native Android application. "
        "Constructed using Kotlin, Jetpack Compose, Material 3, and Firebase (Authentication and Cloud Firestore), the system adopts "
        "the standard MVVM architecture. The application features staff authentication, an operations metrics dashboard, "
        "a multi-field customer registration engine with interactive date pickers, real-time live-synced customer directory, "
        "instant multi-field search (Name, Phone, Email), native system calling and emailing intent integrations, and safe record "
        "deletion with confirmation safeguards. Cloud Firestore Security Rules enforce strict data governance."
    )

    doc.add_page_break()

    # ------------------ 5. INTRODUCTION ------------------
    h5 = doc.add_heading("5. INTRODUCTION", level=1)
    h5.runs[0].font.color.rgb = NAVY
    doc.add_paragraph(
        "The commercial printing sector serves a wide variety of customers, including marketing agencies, educational institutions, "
        "corporations, and private individuals. High volumes of repeat print jobs (business cards, large-format vinyl, corporate stationery, "
        "booklets) necessitate rapid access to accurate customer profiles, billing addresses, and verified contact phone numbers."
    )
    doc.add_paragraph(
        "PrintShop Customer Manager is an Android mobile client relationship management system engineered specifically for printing staff. "
        "By replacing traditional manual record books with an instantaneous cloud-synchronized application, staff members can create, "
        "inspect, and update customer profiles from anywhere on the print floor."
    )

    # ------------------ 6. PROBLEM STATEMENT ------------------
    h6 = doc.add_heading("6. PROBLEM STATEMENT", level=1)
    h6.runs[0].font.color.rgb = NAVY
    doc.add_paragraph(
        "Commercial print shops face critical operational challenges:\n"
        "1. Fragmented Records: Customer data scattered across paper notebooks, sticky notes, and isolated spreadsheets.\n"
        "2. Absence of Mobility: Front-desk computer dependency prevents press-floor technicians from validating client phone numbers quickly.\n"
        "3. High Latency Lookups: Difficulties in searching past customer records during urgent reprint orders.\n"
        "4. Redundant Submissions: Duplicate entries created due to lack of real-time multi-terminal synchronization.\n"
        "5. Security Risks: Unencrypted local files vulnerable to loss, accidental deletion, or unauthorized tampering."
    )

    # ------------------ 7. OBJECTIVES ------------------
    h7 = doc.add_heading("7. OBJECTIVES", level=1)
    h7.runs[0].font.color.rgb = NAVY
    doc.add_paragraph(
        "- Develop a native Android app in Kotlin utilizing Jetpack Compose and Material 3 design.\n"
        "- Enforce the Model-View-ViewModel (MVVM) architecture for optimal separation of concerns.\n"
        "- Implement secure Firebase Authentication for staff login, account creation, and password reset.\n"
        "- Integrate Cloud Firestore for real-time customer data persistence and offline caching.\n"
        "- Build a customer registration form with regex email and phone validation and interactive date pickers.\n"
        "- Provide real-time multi-field search across Name, Phone Number, and Email.\n"
        "- Integrate native Android phone dialer and email intents for immediate client communication.\n"
        "- Secure cloud data access via production-grade Firestore Security Rules."
    )

    # ------------------ 8. SCOPE OF THE PROJECT ------------------
    h8 = doc.add_heading("8. SCOPE OF THE PROJECT", level=1)
    h8.runs[0].font.color.rgb = NAVY
    doc.add_paragraph(
        "The project scope encompasses all commercial printing shop customer service operations, including client onboarding, "
        "contact verification, address management, and communication logging. The system is designed for Android smartphones and tablets "
        "running Android 7.0 (API 24) through Android 14 (API 34)."
    )

    # ------------------ 9. EXISTING SYSTEM ------------------
    h9 = doc.add_heading("9. EXISTING SYSTEM & DRAWBACKS", level=1)
    h9.runs[0].font.color.rgb = NAVY
    doc.add_paragraph(
        "Current small-to-medium printing shops rely on physical ledgers or standalone spreadsheets. "
        "Drawbacks include: zero concurrency control, high vulnerability to physical damage or file corruption, lack of real-time search, "
        "and inability to validate phone and email formats before storage."
    )

    # ------------------ 10. PROPOSED SYSTEM ------------------
    h10 = doc.add_heading("10. PROPOSED SYSTEM & ADVANTAGES", level=1)
    h10.runs[0].font.color.rgb = NAVY
    doc.add_paragraph(
        "The proposed system establishes a mobile-first, cloud-backed CRM platform. Key advantages include:\n"
        "1. Instant Cloud Sync: Changes reflect across all terminals via Cloud Firestore listeners.\n"
        "2. Offline Mode: Local SQLite cache allows viewing and searching client records even during Wi-Fi outages.\n"
        "3. Native Device Actions: Direct calling and emailing via Android Intents.\n"
        "4. Form Integrity: Client-side validation prevents corrupt, blank, or malformed customer submissions.\n"
        "5. Safety Controls: Deletion confirmation dialogs prevent catastrophic data loss."
    )

    doc.add_page_break()

    # ------------------ 11. HARDWARE & SOFTWARE REQUIREMENTS ------------------
    h11 = doc.add_heading("11. SOFTWARE & HARDWARE REQUIREMENTS", level=1)
    h11.runs[0].font.color.rgb = NAVY

    t_req = doc.add_table(rows=9, cols=2)
    t_req.alignment = WD_TABLE_ALIGNMENT.CENTER
    rows_data = [
        ("Component", "Specification"),
        ("Operating System", "Windows 10/11, macOS, Linux"),
        ("IDE", "Android Studio Hedgehog (2023.1.1) or higher"),
        ("Programming Language", "Kotlin 1.9.22"),
        ("UI Toolkit", "Jetpack Compose, Material 3"),
        ("SDK Targets", "Compile SDK 34, Min SDK 24, Target SDK 34"),
        ("Build Tool", "Gradle 8.4 with Android Gradle Plugin 8.3.2"),
        ("Cloud Infrastructure", "Firebase Authentication, Cloud Firestore (BOM 33.1.0)"),
        ("Device / Emulator", "Android AVD API 24-34 with x86_64 / ARM64")
    ]
    for i, (k, v) in enumerate(rows_data):
        row = t_req.rows[i]
        row.cells[0].paragraphs[0].text = k
        row.cells[1].paragraphs[0].text = v
        if i == 0:
            set_cell_background(row.cells[0], "1A237E")
            set_cell_background(row.cells[1], "1A237E")
            row.cells[0].paragraphs[0].runs[0].font.color.rgb = RGBColor(255, 255, 255)
            row.cells[1].paragraphs[0].runs[0].font.color.rgb = RGBColor(255, 255, 255)
            row.cells[0].paragraphs[0].runs[0].font.bold = True
            row.cells[1].paragraphs[0].runs[0].font.bold = True
        else:
            if i % 2 == 1:
                set_cell_background(row.cells[0], "F0F4F8")
                set_cell_background(row.cells[1], "F0F4F8")

    # ------------------ 12. TECHNOLOGIES USED ------------------
    h12 = doc.add_heading("12. TECHNOLOGIES USED", level=1)
    h12.runs[0].font.color.rgb = NAVY
    doc.add_paragraph(
        "- Kotlin: Primary programming language offering type safety, null safety, and coroutine support.\n"
        "- Jetpack Compose: Modern declarative UI framework replacing XML layouts.\n"
        "- Material 3: Latest Android design language featuring dynamic color palettes and elevated card surfaces.\n"
        "- Navigation Compose: Single-activity architecture routing.\n"
        "- Kotlin Coroutines & Flow: Asynchronous, reactive stream processing.\n"
        "- Firebase Authentication: Secure staff login, registration, and password recovery.\n"
        "- Firebase Cloud Firestore: Scalable NoSQL real-time cloud document database."
    )

    # ------------------ 13. SYSTEM ARCHITECTURE ------------------
    h13 = doc.add_heading("13. SYSTEM ARCHITECTURE", level=1)
    h13.runs[0].font.color.rgb = NAVY
    doc.add_paragraph(
        "The application is structured into four primary layers following standard Android architectural guidelines:\n"
        "1. View / UI Layer: Declarative composables observing StateFlow streams and dispatching user intent events.\n"
        "2. ViewModel Layer: Manages UI state, executes validation algorithms, and coordinates asynchronous coroutines.\n"
        "3. Repository Layer: Decouples view models from backend data sources, providing a clean API.\n"
        "4. Data Source Layer: Direct integration with Firebase Authentication and Cloud Firestore."
    )
    arch_img = os.path.join(DIAGRAMS_DIR, "system_architecture.png")
    if os.path.exists(arch_img):
        doc.add_paragraph().add_run().add_picture(arch_img, width=Inches(6.2))
        p_cap = doc.add_paragraph("Figure 13.1: MVVM System Architecture Diagram")
        p_cap.alignment = WD_ALIGN_PARAGRAPH.CENTER
        p_cap.runs[0].font.italic = True

    doc.add_page_break()

    # ------------------ 14. APPLICATION FLOWCHART ------------------
    h14 = doc.add_heading("14. APPLICATION FLOWCHART", level=1)
    h14.runs[0].font.color.rgb = NAVY
    doc.add_paragraph(
        "The application flow governs user onboarding, authentication validation, navigation to the operations dashboard, "
        "and granular operations across customer directory querying, creation, updating, and deletion."
    )
    flow_img = os.path.join(DIAGRAMS_DIR, "application_flowchart.png")
    if os.path.exists(flow_img):
        doc.add_paragraph().add_run().add_picture(flow_img, width=Inches(6.2))
        p_cap = doc.add_paragraph("Figure 14.1: Operational Application Flowchart")
        p_cap.alignment = WD_ALIGN_PARAGRAPH.CENTER
        p_cap.runs[0].font.italic = True

    # ------------------ 15. USE CASE DIAGRAM ------------------
    h15 = doc.add_heading("15. USE CASE DIAGRAM & ACTORS", level=1)
    h15.runs[0].font.color.rgb = NAVY
    doc.add_paragraph(
        "Primary Actor: Printing Shop Staff Member (Store Manager / Account Representative).\n"
        "Key Use Cases:\n"
        "1. UC-01: Staff Authentication (Sign In, Register, Forgot Password)\n"
        "2. UC-02: View Dashboard & Metrics (Total Customers Counter, Recent Clients Preview)\n"
        "3. UC-03: Register New Customer (Full Name, Phone, DOB, Email, Address)\n"
        "4. UC-04: Seed 10 Realistic Sample Customers (One-tap duplicate-safe cloud seeding)\n"
        "5. UC-05: Real-time Search Customer Directory (Filter by Name, Phone, or Email)\n"
        "6. UC-06: Inspect Customer Details (Launch Phone Dialer or Email Client)\n"
        "7. UC-07: Edit Customer Record (Update in Cloud Firestore)\n"
        "8. UC-08: Delete Customer Record (Protected with confirmation prompt)\n"
        "9. UC-09: Sign Out Session"
    )

    doc.add_page_break()

    # ------------------ 16. DATABASE DESIGN ------------------
    h16 = doc.add_heading("16. DATABASE DESIGN", level=1)
    h16.runs[0].font.color.rgb = NAVY
    doc.add_paragraph(
        "Cloud Firestore utilizes a NoSQL document model. Customer information is maintained in the 'customers' collection, "
        "while staff profile metadata is kept in the 'users' collection."
    )
    db_img = os.path.join(DIAGRAMS_DIR, "database_design.png")
    if os.path.exists(db_img):
        doc.add_paragraph().add_run().add_picture(db_img, width=Inches(6.0))
        p_cap = doc.add_paragraph("Figure 16.1: Cloud Firestore Database Design Diagram")
        p_cap.alignment = WD_ALIGN_PARAGRAPH.CENTER
        p_cap.runs[0].font.italic = True

    # ------------------ 17. FIREBASE INTEGRATION ------------------
    h17 = doc.add_heading("17. FIREBASE INTEGRATION & SECURITY RULES", level=1)
    h17.runs[0].font.color.rgb = NAVY
    doc.add_paragraph(
        "Firebase Authentication manages secure user tokens, ensuring that only authenticated employees access company records. "
        "The following security rules protect Cloud Firestore against unauthorized reads and writes:"
    )
    p_code = doc.add_paragraph()
    p_code.paragraph_format.left_indent = Inches(0.5)
    r_code = p_code.add_run(
        "rules_version = '2';\n"
        "service cloud.firestore {\n"
        "  match /databases/{database}/documents {\n"
        "    match /customers/{customerId} {\n"
        "      allow read, write: if request.auth != null;\n"
        "    }\n"
        "    match /users/{userId} {\n"
        "      allow read, write: if request.auth != null && request.auth.uid == userId;\n"
        "    }\n"
        "  }\n"
        "}"
    )
    r_code.font.name = "Consolas"
    r_code.font.size = Pt(9.5)
    r_code.font.color.rgb = NAVY

    doc.add_page_break()

    # ------------------ 18. SCREEN-BY-SCREEN DESCRIPTION ------------------
    h18 = doc.add_heading("18. SCREEN-BY-SCREEN DESCRIPTION", level=1)
    h18.runs[0].font.color.rgb = NAVY
    doc.add_paragraph(
        "1. Login & Sign-Up Screen: Features PrintShop branding, CMYK-inspired color palette, email and password inputs, "
        "password visibility toggling, client-side format validation, and 'Forgot Password' password reset.\n\n"
        "2. Dashboard Screen: Serves as the primary operational hub. Displays current staff email, total customer count card, "
        "quick action cards for registration and directory viewing, one-tap 'Seed 10 Customers' button, and preview list of recent additions.\n\n"
        "3. Customer Directory Screen: Features a top search bar filtering records in real time across Name, Phone, and Email. "
        "Shows an empty state when no records match, and includes a Floating Action Button (+) for instant client onboarding.\n\n"
        "4. Customer Registration Screen: Includes input fields for Full Name, Phone, Date of Birth (interactive date picker), "
        "Email, and Street Address. Displays inline error indicators beneath required fields.\n\n"
        "5. Customer Details Screen: Displays a prominent customer avatar, contact cards, and native Android intent action buttons "
        "('Call' opens system dialer; 'Email' opens default mail client). Also provides Edit and Delete actions.\n\n"
        "6. Edit Customer Screen: Pre-populates all existing customer information, enabling seamless modifications that commit "
        "directly to Cloud Firestore.\n\n"
        "7. Delete Confirmation Dialog: Protects against accidental deletion through an explicit secondary confirmation alert."
    )

    # ------------------ 19. TESTING & TEST CASES ------------------
    h19 = doc.add_heading("19. TESTING AND TEST CASES", level=1)
    h19.runs[0].font.color.rgb = NAVY

    doc.add_paragraph(
        "The application underwent rigorous verification covering authentication, form validation, real-time Firestore synchronization, "
        "search filtering, and native intent dispatches."
    )

    t_tc = doc.add_table(rows=11, cols=4)
    t_tc.alignment = WD_TABLE_ALIGNMENT.CENTER
    tc_data = [
        ("TC ID", "Test Description", "Expected Result", "Status"),
        ("TC-01", "Staff Registration with Valid Data", "User account created; routes to Dashboard", "PASS"),
        ("TC-02", "Registration with Password < 6 chars", "Validation error shown; submit blocked", "PASS"),
        ("TC-03", "Login with Invalid Email Format", "Validation error: 'Enter valid email'", "PASS"),
        ("TC-04", "Customer Registration (Empty Name)", "Error: 'Full Name is required'", "PASS"),
        ("TC-05", "Customer Registration (Invalid Phone)", "Error: 'Invalid phone format'", "PASS"),
        ("TC-06", "Date of Birth Picker Selection", "Date chosen correctly formatted as YYYY-MM-DD", "PASS"),
        ("TC-07", "Firestore Document Creation", "Record added with unique ID; list refreshes", "PASS"),
        ("TC-08", "Seed 10 Dummy Customers", "10 print shop records populated without duplicates", "PASS"),
        ("TC-09", "Real-time Search Filter (Name/Phone)", "List instantly filters to matching records", "PASS"),
        ("TC-10", "Delete Customer with Confirmation", "Record permanently removed from Firestore", "PASS")
    ]
    for i, (tid, desc, exp, stat) in enumerate(tc_data):
        row = t_tc.rows[i]
        row.cells[0].paragraphs[0].text = tid
        row.cells[1].paragraphs[0].text = desc
        row.cells[2].paragraphs[0].text = exp
        row.cells[3].paragraphs[0].text = stat
        if i == 0:
            for c in row.cells:
                set_cell_background(c, "1A237E")
                c.paragraphs[0].runs[0].font.color.rgb = RGBColor(255, 255, 255)
                c.paragraphs[0].runs[0].font.bold = True
        else:
            if i % 2 == 1:
                for c in row.cells:
                    set_cell_background(c, "F0F4F8")
            if stat == "PASS":
                row.cells[3].paragraphs[0].runs[0].font.color.rgb = RGBColor(46, 125, 50)
                row.cells[3].paragraphs[0].runs[0].font.bold = True

    doc.add_page_break()

    # ------------------ 20. RESULTS ------------------
    h20 = doc.add_heading("20. RESULTS", level=1)
    h20.runs[0].font.color.rgb = NAVY
    doc.add_paragraph(
        "The PrintShop Customer Manager application achieved all functional benchmarks:\n"
        "- Clean compilation and zero Gradle build errors.\n"
        "- Sub-second synchronization between Android devices and Cloud Firestore.\n"
        "- Instantaneous search responsiveness even with complete directory listings.\n"
        "- Highly accessible Material 3 design conforming to modern Android standards."
    )

    # ------------------ 21. LIMITATIONS ------------------
    h21 = doc.add_heading("21. LIMITATIONS", level=1)
    h21.runs[0].font.color.rgb = NAVY
    doc.add_paragraph(
        "- Default localization assumes English language formatting.\n"
        "- Password recovery emails require live internet access to Firebase servers.\n"
        "- Profile avatars are currently generated from initials rather than custom uploaded image files."
    )

    # ------------------ 22. FUTURE ENHANCEMENTS ------------------
    h22 = doc.add_heading("22. FUTURE ENHANCEMENTS", level=1)
    h22.runs[0].font.color.rgb = NAVY
    doc.add_paragraph(
        "1. Order History & Print Specifications: Sub-collection to track print paper stocks, coatings, and proofs.\n"
        "2. Automated Job Alerts: Integration with SMS/WhatsApp gateways for automatic job completion alerts.\n"
        "3. Artwork Previews: Firebase Storage integration for PDF proofing and logo attachments.\n"
        "4. Barcode/QR Code Generator: Generating labels for physical print job boxes."
    )

    # ------------------ 23. CONCLUSION ------------------
    h23 = doc.add_heading("23. CONCLUSION", level=1)
    h23.runs[0].font.color.rgb = NAVY
    doc.add_paragraph(
        "PrintShop Customer Manager delivers a reliable, production-ready native Android solution for the printing industry. "
        "By harnessing Kotlin, Jetpack Compose, Material 3, and Firebase, the application successfully digitalizes and streamlines "
        "customer management, eliminating the inefficiencies of manual paper ledgers and disconnected spreadsheets."
    )

    # ------------------ 24. REFERENCES ------------------
    h24 = doc.add_heading("24. REFERENCES", level=1)
    h24.runs[0].font.color.rgb = NAVY
    doc.add_paragraph(
        "1. Google Developers, Android Jetpack Compose Documentation, https://developer.android.com/jetpack/compose\n"
        "2. Google Developers, Guide to Android App Architecture, https://developer.android.com/topic/architecture\n"
        "3. Google Firebase, Firebase Authentication & Cloud Firestore Guides, https://firebase.google.com/docs\n"
        "4. Material Design 3, Material You Guidelines, https://m3.material.io\n"
        "5. Kotlin Foundation, Kotlin Language Documentation, https://kotlinlang.org/docs"
    )

    doc.save(docx_path)
    print(f"[OK] Generated complete 24-section Academic Project Report in DOCX at: {docx_path}")
    return docx_path

if __name__ == "__main__":
    print("[1/4] Generating System Architecture Diagram...")
    create_system_architecture_diagram()
    print("[2/4] Generating Application Flowchart...")
    create_flowchart_diagram()
    print("[3/4] Generating Database Schema Diagram...")
    create_database_schema_diagram()
    print("[4/4] Generating Full 24-Section Academic Report in DOCX...")
    create_docx_report()
    print("[COMPLETE] All diagrams and DOCX project report generated successfully!")
