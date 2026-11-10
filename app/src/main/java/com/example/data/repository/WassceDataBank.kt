package com.example.data.repository

import com.example.data.model.*

object WassceDataBank {

    val subjects = listOf(
        // Core Subjects
        Subject(
            id = "core_math",
            name = "Core Mathematics",
            code = "C-MATH",
            category = SubjectCategory.CORE,
            tracks = listOf(ShsTrack.ALL_ROUND, ShsTrack.GENERAL_SCIENCE, ShsTrack.BUSINESS, ShsTrack.GENERAL_ARTS),
            iconName = "Calculate",
            colorHex = "#2563EB", // Blue
            description = "Algebra, Plane Geometry, Mensuration, Statistics, Trigonometry & Matrices"
        ),
        Subject(
            id = "english",
            name = "English Language",
            code = "ENG",
            category = SubjectCategory.CORE,
            tracks = listOf(ShsTrack.ALL_ROUND, ShsTrack.GENERAL_SCIENCE, ShsTrack.BUSINESS, ShsTrack.GENERAL_ARTS),
            iconName = "MenuBook",
            colorHex = "#7C3AED", // Purple
            description = "Comprehension, Summary, Lexis & Structure, Essay Writing & Oral English"
        ),
        Subject(
            id = "int_sci",
            name = "Integrated Science",
            code = "INT-SCI",
            category = SubjectCategory.CORE,
            tracks = listOf(ShsTrack.ALL_ROUND, ShsTrack.GENERAL_SCIENCE, ShsTrack.BUSINESS, ShsTrack.GENERAL_ARTS),
            iconName = "Science",
            colorHex = "#059669", // Emerald
            description = "Diversity of Matter, Cycles, Systems, Energy & Interactions of Matter"
        ),
        Subject(
            id = "social_studies",
            name = "Social Studies",
            code = "SOC-ST",
            category = SubjectCategory.CORE,
            tracks = listOf(ShsTrack.ALL_ROUND, ShsTrack.GENERAL_SCIENCE, ShsTrack.BUSINESS, ShsTrack.GENERAL_ARTS),
            iconName = "Public",
            colorHex = "#D97706", // Amber
            description = "The Environment, Governance, Socio-Economic Development & Culture"
        ),

        // Elective Science
        Subject(
            id = "elec_math",
            name = "Elective Mathematics",
            code = "E-MATH",
            category = SubjectCategory.ELECTIVE,
            tracks = listOf(ShsTrack.GENERAL_SCIENCE, ShsTrack.ALL_ROUND),
            iconName = "Functions",
            colorHex = "#1D4ED8", // Navy
            description = "Polynomials, Calculus, Coordinate Geometry, Mechanics & Statistics"
        ),
        Subject(
            id = "physics",
            name = "Physics",
            code = "PHY",
            category = SubjectCategory.ELECTIVE,
            tracks = listOf(ShsTrack.GENERAL_SCIENCE, ShsTrack.ALL_ROUND),
            iconName = "Bolt",
            colorHex = "#0284C7", // Sky Blue
            description = "Mechanics, Waves & Optics, Thermodynamics, Electromagnetism & Modern Physics"
        ),
        Subject(
            id = "chemistry",
            name = "Chemistry",
            code = "CHEM",
            category = SubjectCategory.ELECTIVE,
            tracks = listOf(ShsTrack.GENERAL_SCIENCE, ShsTrack.ALL_ROUND),
            iconName = "Biotech",
            colorHex = "#0D9488", // Teal
            description = "Atomic Structure, Periodic Trends, Stoichiometry, Organic & Energetics"
        ),
        Subject(
            id = "biology",
            name = "Biology",
            code = "BIO",
            category = SubjectCategory.ELECTIVE,
            tracks = listOf(ShsTrack.GENERAL_SCIENCE, ShsTrack.ALL_ROUND),
            iconName = "Park",
            colorHex = "#16A34A", // Green
            description = "Cell Biology, Plant & Animal Physiology, Genetics, Evolution & Ecology"
        ),

        // Elective Business
        Subject(
            id = "accounting",
            name = "Financial Accounting",
            code = "ACC",
            category = SubjectCategory.ELECTIVE,
            tracks = listOf(ShsTrack.BUSINESS, ShsTrack.ALL_ROUND),
            iconName = "AccountBalance",
            colorHex = "#EA580C", // Orange
            description = "Ledgers, Final Accounts, Partnerships, Company Accounts & Ratios"
        ),
        Subject(
            id = "biz_mgmt",
            name = "Business Management",
            code = "BM",
            category = SubjectCategory.ELECTIVE,
            tracks = listOf(ShsTrack.BUSINESS, ShsTrack.ALL_ROUND),
            iconName = "BusinessCenter",
            colorHex = "#9333EA", // Violet
            description = "Principles of Management, Marketing, HRM, Production & Law"
        ),

        // Elective Arts
        Subject(
            id = "economics",
            name = "Economics",
            code = "ECON",
            category = SubjectCategory.ELECTIVE,
            tracks = listOf(ShsTrack.BUSINESS, ShsTrack.GENERAL_ARTS, ShsTrack.ALL_ROUND),
            iconName = "TrendingUp",
            colorHex = "#CA8A04", // Gold
            description = "Price Theory, Elasticity, National Income, Public Finance & International Trade"
        ),
        Subject(
            id = "govt",
            name = "Government",
            code = "GOVT",
            category = SubjectCategory.ELECTIVE,
            tracks = listOf(ShsTrack.GENERAL_ARTS, ShsTrack.ALL_ROUND),
            iconName = "Gavel",
            colorHex = "#DC2626", // Red
            description = "Organs of Government, 1992 Constitution, Local Governance & Pan-Africanism"
        )
    )

    val topics = listOf(
        // Core Math Topics
        Topic(
            id = "cm_1",
            subjectId = "core_math",
            unitNumber = 1,
            title = "Algebraic Expressions & Factorization",
            keyPoints = listOf("Expanding binomials", "Difference of two squares", "Quadratic factorisation", "Algebraic fractions"),
            examWeight = ExamWeight.HIGH
        ),
        Topic(
            id = "cm_2",
            subjectId = "core_math",
            unitNumber = 2,
            title = "Number Bases & Modular Arithmetic",
            keyPoints = listOf("Conversions between bases", "Operations in modulo systems", "Modulo arithmetic tables", "Solving congruences"),
            examWeight = ExamWeight.MEDIUM
        ),
        Topic(
            id = "cm_3",
            subjectId = "core_math",
            unitNumber = 3,
            title = "Plane Geometry & Circle Theorems",
            keyPoints = listOf("Angle at center is twice angle at circumference", "Angles in same segment", "Cyclic quadrilaterals", "Tangent-chord theorem"),
            examWeight = ExamWeight.HIGH
        ),
        Topic(
            id = "cm_4",
            subjectId = "core_math",
            unitNumber = 4,
            title = "Mensuration (Perimeter, Area & Volume)",
            keyPoints = listOf("Area and arc length of sector", "Surface area and volume of cones, cylinders, pyramids & spheres", "Frustum calculations"),
            examWeight = ExamWeight.HIGH
        ),
        Topic(
            id = "cm_5",
            subjectId = "core_math",
            unitNumber = 5,
            title = "Trigonometry, Angles of Elevation & Depression",
            keyPoints = listOf("Sine, Cosine & Tangent ratios", "Sine rule and Cosine rule", "Three-figure bearings", "Practical height and distance problems"),
            examWeight = ExamWeight.HIGH
        ),
        Topic(
            id = "cm_6",
            subjectId = "core_math",
            unitNumber = 6,
            title = "Statistics & Probability",
            keyPoints = listOf("Mean, Median & Mode of grouped data", "Cumulative frequency curves (Ogive)", "Standard deviation & variance", "Mutually exclusive and independent probability"),
            examWeight = ExamWeight.HIGH
        ),
        Topic(
            id = "cm_7",
            subjectId = "core_math",
            unitNumber = 7,
            title = "Vectors & Geometric Transformations",
            keyPoints = listOf("Vector addition, scalar multiplication and magnitude", "Reflection in axes and lines", "Rotation about origin", "Enlargement and translation"),
            examWeight = ExamWeight.MEDIUM
        ),

        // English Topics
        Topic(
            id = "eng_1",
            subjectId = "english",
            unitNumber = 1,
            title = "Comprehension & Vocabulary in Context",
            keyPoints = listOf("Literal comprehension", "Inferential reasoning", "Grammatical functions of clauses", "Figures of speech: Metaphor, Simile, Irony"),
            examWeight = ExamWeight.HIGH
        ),
        Topic(
            id = "eng_2",
            subjectId = "english",
            unitNumber = 2,
            title = "Summary Writing & Paraphrasing",
            keyPoints = listOf("Distinguishing main points from illustrations", "Writing in clear simple sentences", "Paraphrasing without extraneous material", "Penalty for lifting"),
            examWeight = ExamWeight.HIGH
        ),
        Topic(
            id = "eng_3",
            subjectId = "english",
            unitNumber = 3,
            title = "Lexis and Structure (Concord & Phrasal Verbs)",
            keyPoints = listOf("Subject-verb agreement", "Conditionals (Type 1, 2, 3)", "Tenses and sequence of tenses", "Prepositional phrases and collocations"),
            examWeight = ExamWeight.HIGH
        ),
        Topic(
            id = "eng_4",
            subjectId = "english",
            unitNumber = 4,
            title = "Formal, Informal Letters & Articles",
            keyPoints = listOf("Salutations and layouts", "Tone and register", "Organisation and paragraphing", "Mechanical accuracy and punctuation"),
            examWeight = ExamWeight.HIGH
        ),
        Topic(
            id = "eng_5",
            subjectId = "english",
            unitNumber = 5,
            title = "Oral English (Phonology & Stress)",
            keyPoints = listOf("Pure vowels vs diphthongs", "Consonant clusters and silent letters", "Syllable stress and word class change", "Intonation patterns"),
            examWeight = ExamWeight.HIGH
        ),

        // Integrated Science Topics
        Topic(
            id = "is_1",
            subjectId = "int_sci",
            unitNumber = 1,
            title = "Diversity of Matter & Particulate Nature",
            keyPoints = listOf("Atomic structure and electronic configurations", "Periodic table trends", "Mixtures, elements and compounds", "Solutions, suspensions and colloids"),
            examWeight = ExamWeight.HIGH
        ),
        Topic(
            id = "is_2",
            subjectId = "int_sci",
            unitNumber = 2,
            title = "Cells, Tissues & Organisation of Life",
            keyPoints = listOf("Plant vs animal cell differences", "Microscopy and organelle functions", "Cellular respiration equation", "Diffusion and osmosis"),
            examWeight = ExamWeight.HIGH
        ),
        Topic(
            id = "is_3",
            subjectId = "int_sci",
            unitNumber = 3,
            title = "Acids, Bases, Salts & Soil Chemistry",
            keyPoints = listOf("pH scale and indicators", "Neutralisation reactions and salt preparation", "Soil formation and nutrients (N, P, K)", "Soil erosion and conservation methods"),
            examWeight = ExamWeight.HIGH
        ),
        Topic(
            id = "is_4",
            subjectId = "int_sci",
            unitNumber = 4,
            title = "Electric Circuits, Ohm's Law & Magnetism",
            keyPoints = listOf("Series and parallel circuit calculations", "Ohm's Law: V = I * R", "Electrical power: P = I * V", "Magnetic fields and electromagnets"),
            examWeight = ExamWeight.HIGH
        ),
        Topic(
            id = "is_5",
            subjectId = "int_sci",
            unitNumber = 5,
            title = "Photosynthesis & Human Reproductive Systems",
            keyPoints = listOf("Light and dark phases of photosynthesis", "Male and female reproductive organs", "Menstrual cycle and hormonal regulation", "STI prevention and genetics"),
            examWeight = ExamWeight.HIGH
        ),

        // Social Studies Topics
        Topic(
            id = "ss_1",
            subjectId = "social_studies",
            unitNumber = 1,
            title = "The Environment & Environmental Degradation",
            keyPoints = listOf("Deforestation and illegal mining (Galamsey)", "Pollution of water bodies (Pra, Birim)", "Environmental protection agencies", "Sustainable resource exploitation"),
            examWeight = ExamWeight.HIGH
        ),
        Topic(
            id = "ss_2",
            subjectId = "social_studies",
            unitNumber = 2,
            title = "Governance & The 1992 Constitution",
            keyPoints = listOf("Organs of state: Executive, Legislature, Judiciary", "Checks and balances", "Fundamental human rights and freedoms", "Citizenship and civic responsibilities"),
            examWeight = ExamWeight.HIGH
        ),
        Topic(
            id = "ss_3",
            subjectId = "social_studies",
            unitNumber = 3,
            title = "Socio-Economic Development of Ghana",
            keyPoints = listOf("Human resource development and training", "Agriculture vs industrialization", "Infrastructure and capital formation", "Tourism and cultural promotion"),
            examWeight = ExamWeight.MEDIUM
        ),

        // Elective Maths Topics
        Topic(
            id = "em_1",
            subjectId = "elec_math",
            unitNumber = 1,
            title = "Polynomials, Partial Fractions & Surds",
            keyPoints = listOf("Remainder and Factor Theorems", "Division of polynomials", "Decomposition into partial fractions", "Rationalising surds"),
            examWeight = ExamWeight.HIGH
        ),
        Topic(
            id = "em_2",
            subjectId = "elec_math",
            unitNumber = 2,
            title = "Differentiation & Applications",
            keyPoints = listOf("Product, Quotient and Chain rules", "Tangents and normals to curves", "Stationary points and curve sketching", "Rates of change and kinematics"),
            examWeight = ExamWeight.HIGH
        ),
        Topic(
            id = "em_3",
            subjectId = "elec_math",
            unitNumber = 3,
            title = "Integration & Area Under Curves",
            keyPoints = listOf("Standard integrals and substitution", "Definite integrals", "Area bounded by curves and axes", "Volume of solids of revolution"),
            examWeight = ExamWeight.HIGH
        ),
        Topic(
            id = "em_4",
            subjectId = "elec_math",
            unitNumber = 4,
            title = "Coordinate Geometry (Circles & Conics)",
            keyPoints = listOf("Equation of a circle: (x-a)^2 + (y-b)^2 = r^2", "General equation x^2 + y^2 + 2gx + 2fy + c = 0", "Length of tangent from external point", "Intersection of line and circle"),
            examWeight = ExamWeight.HIGH
        ),

        // Physics Topics
        Topic(
            id = "phy_1",
            subjectId = "physics",
            unitNumber = 1,
            title = "Mechanics & Newton's Laws of Motion",
            keyPoints = listOf("Equations of motion with uniform acceleration", "Newton's 1st, 2nd, 3rd laws", "Momentum and impulse conservation", "Work, energy and power theorem"),
            examWeight = ExamWeight.HIGH
        ),
        Topic(
            id = "phy_2",
            subjectId = "physics",
            unitNumber = 2,
            title = "Waves, Sound & Physical Optics",
            keyPoints = listOf("Progressive wave equation y = A sin(wt - kx)", "Stationary waves in strings and air columns", "Snell's law, critical angle and total internal reflection", "Diffraction and interference of light"),
            examWeight = ExamWeight.HIGH
        ),

        // Chemistry Topics
        Topic(
            id = "chem_1",
            subjectId = "chemistry",
            unitNumber = 1,
            title = "Atomic Structure, Periodicity & Chemical Bonding",
            keyPoints = listOf("Quantum numbers and orbital shapes", "Ionization energy and electron affinity trends", "Ionic, covalent and coordinate covalent bonding", "VSEPR theory and molecular geometries"),
            examWeight = ExamWeight.HIGH
        ),
        Topic(
            id = "chem_2",
            subjectId = "chemistry",
            unitNumber = 2,
            title = "Organic Chemistry (Hydrocarbons & Functional Groups)",
            keyPoints = listOf("Alkanes, Alkenes and Alkynes reactions", "Homologous series and isomerism", "Alkanols, Alkanoic acids and Esters", "Saponification and polymerization"),
            examWeight = ExamWeight.HIGH
        ),

        // Biology Topics
        Topic(
            id = "bio_1",
            subjectId = "biology",
            unitNumber = 1,
            title = "Genetics, Heredity & Mendel's Laws",
            keyPoints = listOf("Monohybrid and dihybrid crosses", "Codominance, multiple alleles and ABO blood groups", "Sex-linked traits (Haemophilia, Colour blindness)", "DNA replication and protein synthesis"),
            examWeight = ExamWeight.HIGH
        ),

        // Economics Topics
        Topic(
            id = "econ_1",
            subjectId = "economics",
            unitNumber = 1,
            title = "Price Theory: Demand, Supply & Equilibrium",
            keyPoints = listOf("Law of Demand and Supply", "Price, income and cross elasticity formulas", "Consumer surplus and producer surplus", "Government price controls: ceilings and floors"),
            examWeight = ExamWeight.HIGH
        ),
        Topic(
            id = "econ_2",
            subjectId = "economics",
            unitNumber = 2,
            title = "National Income & Public Finance in Ghana",
            keyPoints = listOf("GDP, GNP and NNI calculation methods", "Circular flow of income", "Direct vs indirect taxes", "Fiscal policy and public debt management"),
            examWeight = ExamWeight.HIGH
        ),

        // Financial Accounting Topics
        Topic(
            id = "acc_1",
            subjectId = "accounting",
            unitNumber = 1,
            title = "Control Accounts & Bank Reconciliation",
            keyPoints = listOf("Sales and Purchases ledger control accounts", "Bank reconciliation statements", "Dishonoured cheques and uncredited lodgements", "Error correction through suspense accounts"),
            examWeight = ExamWeight.HIGH
        ),

        // Government Topics
        Topic(
            id = "govt_1",
            subjectId = "govt",
            unitNumber = 1,
            title = "The 1992 Constitution & Local Governance",
            keyPoints = listOf("Features of Ghana's 1992 Fourth Republican Constitution", "Council of State and independent commissions (EC, CHRAJ)", "District Assemblies Common Fund (DACF)", "Decentralisation structure"),
            examWeight = ExamWeight.HIGH
        )
    )

    val pastQuestions = listOf(
        // Core Math 2024
        PastQuestion(
            id = "pq_cm_2024_1",
            subjectId = "core_math",
            year = 2024,
            questionNumber = 1,
            topicTitle = "Algebraic Expressions",
            questionText = "If 3^(2x - 1) = 81, find the value of x.",
            options = listOf("2", "2.5", "3", "4"),
            correctOptionIndex = 1,
            explanation = "Express both sides in base 3: 81 = 3^4. Therefore, 2x - 1 = 4. Adding 1 gives 2x = 5, which means x = 5/2 = 2.5.",
            examinerTip = "Students often forget to equate exponents only after ensuring both bases are identical prime powers."
        ),
        PastQuestion(
            id = "pq_cm_2024_2",
            subjectId = "core_math",
            year = 2024,
            questionNumber = 2,
            topicTitle = "Plane Geometry",
            questionText = "In a circle with centre O, the angle subtended by an arc at the circumference is 48°. What is the angle subtended by the same arc at the centre?",
            options = listOf("24°", "48°", "96°", "132°"),
            correctOptionIndex = 2,
            explanation = "By circle theorem: The angle subtended by an arc at the centre is twice the angle subtended by it at any point on the remaining part of the circumference. Angle at centre = 2 * 48° = 96°.",
            examinerTip = "A very frequent Paper 1 question. Do not confuse doubling with halving."
        ),
        PastQuestion(
            id = "pq_cm_2023_1",
            subjectId = "core_math",
            year = 2023,
            questionNumber = 14,
            topicTitle = "Number Bases",
            questionText = "Solve for y in the modular equation: 3y ≡ 4 (mod 7).",
            options = listOf("y ≡ 2 (mod 7)", "y ≡ 3 (mod 7)", "y ≡ 6 (mod 7)", "y ≡ 5 (mod 7)"),
            correctOptionIndex = 2,
            explanation = "We seek an integer y such that 3y - 4 is divisible by 7. Testing values: If y=6, 3(6) = 18. 18 = 2(7) + 4. Thus 18 ≡ 4 (mod 7). Hence y ≡ 6 (mod 7).",
            examinerTip = "In modulo arithmetic, test possible residues {0,1,2,3,4,5,6} sequentially or multiply both sides by the modular inverse of 3 modulo 7 (which is 5)."
        ),
        PastQuestion(
            id = "pq_cm_2022_1",
            subjectId = "core_math",
            year = 2022,
            questionNumber = 22,
            topicTitle = "Trigonometry",
            questionText = "A flag pole of height 12 m casts a shadow of 12√3 m on level ground. What is the angle of elevation of the sun?",
            options = listOf("30°", "45°", "60°", "75°"),
            correctOptionIndex = 0,
            explanation = "Tan θ = (Opposite / Adjacent) = 12 / (12√3) = 1 / √3. Since tan 30° = 1/√3, the angle of elevation is 30°.",
            examinerTip = "Special angles (30°, 45°, 60°) are frequently tested without calculators. Remember tan 30° = 1/√3, tan 60° = √3."
        ),

        // English Language
        PastQuestion(
            id = "pq_eng_2024_1",
            subjectId = "english",
            year = 2024,
            questionNumber = 3,
            topicTitle = "Concord & Lexis",
            questionText = "The Principal, as well as the teachers, ________ at the assembly yesterday.",
            options = listOf("are present", "was present", "were present", "have been present"),
            correctOptionIndex = 1,
            explanation = "When a subject is followed by phrases such as 'as well as', 'together with', or 'in addition to', the verb agrees with the principal subject ('The Principal', singular). Hence 'was present'.",
            examinerTip = "Chief Examiner report warns that candidates are consistently misled by plural nouns occurring inside parenthetical phrases."
        ),
        PastQuestion(
            id = "pq_eng_2023_1",
            subjectId = "english",
            year = 2023,
            questionNumber = 18,
            topicTitle = "Idiomatic Expressions",
            questionText = "Kwame was asked to 'call it a day'. This means Kwame should:",
            options = listOf("Shout during daylight", "Stop working on the task", "Schedule another day", "Celebrate the occasion"),
            correctOptionIndex = 1,
            explanation = "The idiom 'to call it a day' means to stop what you are doing, often because you have finished or are tired.",
            examinerTip = "WAEC tests idioms in Section 4 of Paper 1; review common everyday idioms."
        ),

        // Integrated Science
        PastQuestion(
            id = "pq_is_2024_1",
            subjectId = "int_sci",
            year = 2024,
            questionNumber = 5,
            topicTitle = "Diversity of Matter",
            questionText = "An atom of an element X has atomic number 17 and mass number 35. The number of neutrons in the nucleus of X is:",
            options = listOf("17", "18", "35", "52"),
            correctOptionIndex = 1,
            explanation = "Number of neutrons = Mass Number (A) - Atomic Number (Z) = 35 - 17 = 18.",
            examinerTip = "Straightforward calculation: Mass number is total nucleons (protons + neutrons); atomic number is protons."
        ),
        PastQuestion(
            id = "pq_is_2023_1",
            subjectId = "int_sci",
            year = 2023,
            questionNumber = 12,
            topicTitle = "Acids, Bases & Salts",
            questionText = "Which of the following compounds is an example of an amphoteric oxide?",
            options = listOf("CaO", "CO2", "Al2O3", "SO3"),
            correctOptionIndex = 2,
            explanation = "Aluminium oxide (Al2O3) and Zinc oxide (ZnO) react with both acids and bases to produce salt and water, exhibiting amphoteric properties.",
            examinerTip = "Remember amphoteric oxides: Aluminium oxide, Zinc oxide, Lead(II) oxide."
        ),

        // Elective Maths
        PastQuestion(
            id = "pq_em_2024_1",
            subjectId = "elec_math",
            year = 2024,
            questionNumber = 8,
            topicTitle = "Calculus & Differentiation",
            questionText = "Find the gradient of the curve y = 2x^3 - 5x^2 + 4 at the point where x = 2.",
            options = listOf("4", "6", "8", "12"),
            correctOptionIndex = 0,
            explanation = "dy/dx = 6x^2 - 10x. At x = 2, dy/dx = 6(2^2) - 10(2) = 6(4) - 20 = 24 - 20 = 4.",
            examinerTip = "Differentiate each term correctly using power rule d/dx(x^n) = n*x^(n-1)."
        ),

        // Physics
        PastQuestion(
            id = "pq_phy_2024_1",
            subjectId = "physics",
            year = 2024,
            questionNumber = 4,
            topicTitle = "Mechanics",
            questionText = "A body of mass 5 kg initially at rest is acted upon by a constant force of 20 N for 4 seconds. The final velocity acquired is:",
            options = listOf("10 m/s", "16 m/s", "20 m/s", "80 m/s"),
            correctOptionIndex = 1,
            explanation = "Acceleration a = F / m = 20 N / 5 kg = 4 m/s^2. Using v = u + at with u = 0, v = 0 + (4 m/s^2 * 4 s) = 16 m/s.",
            examinerTip = "Always state Newton's second law F = ma and uniform acceleration formulas clearly."
        ),

        // Chemistry
        PastQuestion(
            id = "pq_chem_2024_1",
            subjectId = "chemistry",
            year = 2024,
            questionNumber = 7,
            topicTitle = "Chemical Bonding",
            questionText = "What is the shape of a water (H2O) molecule according to the VSEPR theory?",
            options = listOf("Linear", "Trigonal planar", "Tetrahedral", "Bent / V-shaped"),
            correctOptionIndex = 3,
            explanation = "Oxygen has 6 valence electrons; in H2O it forms 2 single covalent bonds and retains 2 lone pairs. The 4 electron pairs arrange tetrahedrally, but the molecular geometry is bent (approx 104.5°).",
            examinerTip = "Candidates must distinguish between electron pair geometry (tetrahedral) and actual molecular shape (bent/V-shaped)."
        ),

        // Economics
        PastQuestion(
            id = "pq_econ_2024_1",
            subjectId = "economics",
            year = 2024,
            questionNumber = 11,
            topicTitle = "Price Theory",
            questionText = "When the percentage change in quantity demanded is greater than the percentage change in price, demand is said to be:",
            options = listOf("Perfecty inelastic", "Inelastic", "Unitary elastic", "Price elastic"),
            correctOptionIndex = 3,
            explanation = "Price elasticity of demand |Ed| > 1 indicates price elastic demand, meaning consumers are very responsive to price changes.",
            examinerTip = "Commit the elasticity thresholds (|Ed| > 1 elastic, |Ed| < 1 inelastic, |Ed| = 1 unitary) to memory."
        ),

        // Financial Accounting
        PastQuestion(
            id = "pq_acc_2024_1",
            subjectId = "accounting",
            year = 2024,
            questionNumber = 6,
            topicTitle = "Bank Reconciliation",
            questionText = "An uncredited cheque of GH¢ 5,000 paid into the bank but not yet reflected in the bank statement should be:",
            options = listOf("Added to the cash book balance", "Deducted from the cash book balance", "Added to the bank statement balance", "Deducted from the bank statement balance"),
            correctOptionIndex = 2,
            explanation = "When starting reconciliation from bank statement balance to reach cash book balance, uncredited cheques (lodgements not yet credited) are added to the bank statement balance.",
            examinerTip = "Understand whether reconciliation starts from Cash Book or Bank Statement before determining add/subtract."
        ),

        // Government
        PastQuestion(
            id = "pq_govt_2024_1",
            subjectId = "govt",
            year = 2024,
            questionNumber = 9,
            topicTitle = "1992 Constitution",
            questionText = "Under the 1992 Constitution of Ghana, who appoints the Chief Justice of the Republic?",
            options = listOf(
                "The Council of State acting alone",
                "The President acting in consultation with the Council of State and with approval of Parliament",
                "The Judicial Council with two-thirds majority",
                "The Speaker of Parliament"
            ),
            correctOptionIndex = 1,
            explanation = "Article 144(1) of the 1992 Constitution: The Chief Justice is appointed by the President acting in consultation with the Council of State and with the approval of Parliament.",
            examinerTip = "High-frequency question in WASSCE Government Paper 1 regarding constitutional appointments."
        )
    )

    val bundledTextbooks = listOf(
        TextbookDocument(
            id = "tb_aki_ola_math",
            title = "Aki-Ola Core Mathematics for SHS",
            subjectId = "core_math",
            authorOrSource = "Peter Asiedu (Aki-Ola Publications)",
            isBundled = true,
            pageCount = 380,
            sampleChapterTitle = "Chapter 4: Quadratic Equations & Circle Geometry",
            sampleChapterExcerpt = """
                QUADRATIC EQUATIONS AND CIRCLE THEOREMS FOR WASSCE
                
                A quadratic equation in standard form is written as ax² + bx + c = 0, where a ≠ 0.
                Solving Methods:
                1. Factorisation: Used when the roots are rational numbers. Split the middle term bx into two terms whose product is a*c.
                2. Quadratic Formula: x = [-b ± √(b² - 4ac)] / (2a). The discriminant Δ = b² - 4ac determines root nature:
                   - Δ > 0: Two distinct real roots.
                   - Δ = 0: Equal/repeated real roots.
                   - Δ < 0: Complex conjugate roots (no real roots).
                3. Completing the Square: Essential technique for deriving vertex form y = a(x - h)² + k.
                
                CIRCLE GEOMETRY THEOREMS (WAEC Paper 2 High Yield):
                Theorem 1: The angle subtended by an arc at the centre is twice the angle subtended by it at any point on the circumference.
                Theorem 2: Angles in the same segment of a circle are equal.
                Theorem 3: The opposite angles of a cyclic quadrilateral are supplementary (sum to 180°).
                Theorem 4: The angle between a tangent and a chord through the point of contact is equal to the angle subtended by the chord in the alternate segment.
                
                CHIEF EXAMINER NOTES: Candidates frequently lose marks on geometry questions by stating numeric answers without providing geometric reasons in brackets (e.g., '[Opp. angles of cyclic quad. are supplementary]').
            """.trimIndent()
        ),
        TextbookDocument(
            id = "tb_ababio_chem",
            title = "New School Chemistry for Senior Secondary",
            subjectId = "chemistry",
            authorOrSource = "Osei Yaw Ababio (Africana First)",
            isBundled = true,
            pageCount = 520,
            sampleChapterTitle = "Chapter 7: Chemical Bonding & Molecular Structures",
            sampleChapterExcerpt = """
                CHEMICAL BONDING AND INTERMOLECULAR FORCES
                
                Atoms bond in order to achieve the stable octet or duplet electronic configuration of noble gases.
                
                1. Electrovalent (Ionic) Bonding:
                - Transfer of electrons from metallic element of low ionization energy to non-metal of high electron affinity.
                - Characterized by high melting/boiling points, electrical conductivity in molten/aqueous states due to mobile ions.
                - Examples: NaCl, MgO, CaF2.
                
                2. Covalent Bonding:
                - Mutual sharing of electron pairs between non-metallic atoms.
                - Coordinate (Dative) covalent bond: Both shared electrons are contributed by only one of the participating atoms (e.g. NH4+, H3O+).
                
                3. VSEPR Theory (Valence Shell Electron Pair Repulsion):
                - Electron pairs repel one another and position themselves as far apart as possible to minimize repulsive energy.
                - Repulsion hierarchy: Lone Pair - Lone Pair > Lone Pair - Bond Pair > Bond Pair - Bond Pair.
                - H2O is bent (104.5°) due to 2 lone pairs on Oxygen. NH3 is trigonal pyramidal (107°) with 1 lone pair on Nitrogen. CH4 is regular tetrahedral (109.5°).
                
                4. Intermolecular Forces:
                - Hydrogen bonding: Electrostatic attraction between H bonded to highly electronegative small atom (N, O, F) and a lone pair on another electronegative atom. Explains why water is liquid at room temperature while H2S is gas.
            """.trimIndent()
        ),
        TextbookDocument(
            id = "tb_int_sci_africa",
            title = "Integrated Science for Senior High Schools",
            subjectId = "int_sci",
            authorOrSource = "Ministry of Education / GES Curriculum",
            isBundled = true,
            pageCount = 410,
            sampleChapterTitle = "Unit 3: Photosynthesis, Respiration & Ecosystems",
            sampleChapterExcerpt = """
                PHOTOSYNTHESIS AND ENERGY FLOW IN ECOSYSTEMS
                
                Photosynthesis is the photochemical synthesis of glucose from carbon dioxide and water in the presence of sunlight and chlorophyll.
                Overall Equation: 6CO2 + 6H2O --[Light & Chlorophyll]--> C6H12O6 + 6O2
                
                Two Successive Phases:
                1. Light Stage (Photolysis):
                   - Occurs in thylakoid membranes (grana) of chloroplasts.
                   - Light energy absorbed by chlorophyll splits water: 2H2O -> 4H+ + 4e- + O2.
                   - Generates ATP and NADPH for the dark stage.
                2. Dark Stage (Calvin Cycle / Carbon Fixation):
                   - Occurs in the stroma of chloroplasts.
                   - Enzymes fix CO2 into glucose utilizing ATP and NADPH from light stage.
                
                Factors affecting rate: Light intensity, CO2 concentration, Temperature.
                
                Human Respiratory Contrast:
                Aerobic: C6H12O6 + 6O2 -> 6CO2 + 6H2O + 38 ATP.
                Anaerobic (Lactic fermentation in muscles): C6H12O6 -> 2 Lactic Acid + 2 ATP.
            """.trimIndent()
        ),
        TextbookDocument(
            id = "tb_econ_shs",
            title = "Principles of Economics for West Africa",
            subjectId = "economics",
            authorOrSource = "K. A. Baah-Nuakoh & W. B. Dadson",
            isBundled = true,
            pageCount = 340,
            sampleChapterTitle = "Chapter 3: Price Elasticity of Demand & Supply",
            sampleChapterExcerpt = """
                PRICE ELASTICITY OF DEMAND (PED) AND GOVERNMENT TAXATION
                
                Price Elasticity of Demand measures the degree of responsiveness of quantity demanded of a commodity to a change in its price.
                Formula: PED = (% Change in Quantity Demanded) / (% Change in Price)
                = (ΔQ / Q1) / (ΔP / P1)
                
                Elasticity Degrees:
                1. Perfectly Inelastic (|PED| = 0): Vertical demand curve. Quantity demanded remains unchanged regardless of price (e.g. insulin, life-saving medicines).
                2. Inelastic (0 < |PED| < 1): Steep curve. % change in Q < % change in P. Typical of basic necessities in Ghana (e.g. gari, salt, water).
                3. Unitary Elastic (|PED| = 1): Rectangular hyperbola. % change in Q = % change in P. Total revenue remains constant.
                4. Elastic (|PED| > 1): Gentle slope. % change in Q > % change in P. Luxuries with close substitutes.
                5. Perfectly Elastic (|PED| = ∞): Horizontal line. Any price increase drops demand to zero.
                
                Chief Examiner Insight: Whenever calculating elasticity, students must show step-by-step working including initial and final quantities. Note whether the sign is negative or absolute value.
            """.trimIndent()
        )
    )

    val studyBuddies = listOf(
        StudyBuddy(
            id = "buddy_1",
            name = "Kofi Mensah",
            school = "PRESEC - Legon",
            avatarInitials = "KM",
            avatarColorHex = "#2563EB",
            weeklyXp = 2450,
            streakDays = 14,
            activeSubject = "Elective Maths",
            statusMessage = "Solving 2023 Calculus Pasco! Aggregate 6 loading...",
            isOnline = true
        ),
        StudyBuddy(
            id = "buddy_2",
            name = "Akosua Serwaa",
            school = "Wesley Girls' High School",
            avatarInitials = "AS",
            avatarColorHex = "#7C3AED",
            weeklyXp = 2380,
            streakDays = 19,
            activeSubject = "Chemistry",
            statusMessage = "Memorizing Organic functional groups for Paper 2",
            isOnline = true
        ),
        StudyBuddy(
            id = "buddy_3",
            name = "Kwabena Boateng",
            school = "Prempeh College",
            avatarInitials = "KB",
            avatarColorHex = "#059669",
            weeklyXp = 2120,
            streakDays = 9,
            activeSubject = "Physics",
            statusMessage = "Ready for 1v1 Quiz Battle in Mechanics!",
            isOnline = true
        ),
        StudyBuddy(
            id = "buddy_4",
            name = "Efua Appiah",
            school = "Achimota School",
            avatarInitials = "EA",
            avatarColorHex = "#D97706",
            weeklyXp = 1950,
            streakDays = 12,
            activeSubject = "Economics",
            statusMessage = "Study room open: Macroeconomics group discussion",
            isOnline = false
        ),
        StudyBuddy(
            id = "buddy_5",
            name = "Yaw Frimpong",
            school = "Mfantsipim School",
            avatarInitials = "YF",
            avatarColorHex = "#DC2626",
            weeklyXp = 1890,
            streakDays = 7,
            activeSubject = "Core Mathematics",
            statusMessage = "Drilling Circle Theorems & Bearing questions",
            isOnline = true
        ),
        StudyBuddy(
            id = "buddy_6",
            name = "Nana Ama Osei",
            school = "Holy Child School",
            avatarInitials = "NO",
            avatarColorHex = "#0284C7",
            weeklyXp = 1840,
            streakDays = 11,
            activeSubject = "Financial Accounting",
            statusMessage = "Balancing Control Accounts & Balance Sheets",
            isOnline = true
        ),
        StudyBuddy(
            id = "buddy_7",
            name = "Selorm Agbenu",
            school = "Opoku Ware School (OWASS)",
            avatarInitials = "SA",
            avatarColorHex = "#9333EA",
            weeklyXp = 1760,
            streakDays = 8,
            activeSubject = "Social Studies",
            statusMessage = "Revising 1992 Fourth Republic Constitution",
            isOnline = false
        )
    )

    val studyRooms = listOf(
        StudyRoom(
            id = "room_presec",
            roomCode = "PRESEC-24",
            title = "Science Prep: Calculus & Mechanics",
            subjectName = "Elective Maths & Physics",
            participantCount = 18,
            pomodoroMinutesLeft = 19,
            hostName = "Kofi Mensah",
            school = "PRESEC Legon"
        ),
        StudyRoom(
            id = "room_wghs",
            roomCode = "WEY-GEY-01",
            title = "A1 Drill: Organic Chemistry & Biology",
            subjectName = "Chemistry & Biology",
            participantCount = 24,
            pomodoroMinutesLeft = 12,
            hostName = "Akosua Serwaa",
            school = "Wesley Girls' High"
        ),
        StudyRoom(
            id = "room_biz",
            roomCode = "BIZ-HEROES",
            title = "Accounting & Business Management Night Prep",
            subjectName = "Financial Accounting",
            participantCount = 14,
            pomodoroMinutesLeft = 23,
            hostName = "Nana Ama Osei",
            school = "Holy Child"
        ),
        StudyRoom(
            id = "room_core",
            roomCode = "CORE-PASS",
            title = "Core Maths & English Speed Drill",
            subjectName = "Core Mathematics",
            participantCount = 32,
            pomodoroMinutesLeft = 8,
            hostName = "Yaw Frimpong",
            school = "Mfantsipim"
        )
    )
}
