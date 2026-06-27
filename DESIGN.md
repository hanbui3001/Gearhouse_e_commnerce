# Design System: TECH_SPEC Commerce

## 1. Visual Theme & Atmosphere

TECH_SPEC is a premium Vietnamese technology commerce interface for curated studio hardware, mobile work kits, audio gear, keyboards, displays, docks, and accessories. The mood is warm, editorial, quiet, and tactile: like a well-arranged product studio rather than a loud electronics marketplace.

- **Density:** 4/10 — airy but still practical for ecommerce scanning.
- **Variance:** 7/10 — asymmetric hero, editorial bento discovery, sticky narrative panels, horizontal product shelves.
- **Motion:** 6/10 — restrained scroll reveals, carousel movement, and subtle floating brand tiles.
- **Language:** Vietnamese interface copy by default. Buttons should use direct commerce language such as "Vào cửa hàng", "Xem sản phẩm", "Thêm vào giỏ", and "Liên hệ hỗ trợ".
- **Commerce Positioning:** Curated hardware, not comparison marketplace. Avoid compare-focused flows and remove "Compare" buttons entirely.

The interface should feel broad and immersive, using nearly the full viewport width with restrained page padding. Content should not appear trapped in a narrow centered column.

## 2. Color Palette & Roles

- **Warm Canvas** (#F7F2E9) — Main page background. Soft editorial warmth.
- **Paper Surface** (#FBF8F1) — Header, section shells, brand tiles, and high-level panels.
- **Milk Surface** (#FFFDF8) — Inner panel fill for section headers and content cores.
- **Charcoal Ink** (#171410) — Primary text and primary button fill. Never use pure black.
- **Deep Cocoa** (#2F2217) — Text on warm accent buttons.
- **Muted Clay** (#6E6255) — Body copy, descriptions, footer text.
- **Weathered Taupe** (#8A7D6C) — Metadata, search hints, secondary UI labels.
- **Brass Accent** (#D6A15E) — The single accent. Used for badges, focus rings, active emphasis, and small mono labels.
- **Brass Surface** (#EFE1C7) — Secondary CTA fill. It replaces washed-out white outline buttons.
- **Brass Hover** (#E4CD9F) — Secondary CTA hover fill.
- **Warm Border** (#E4DDD0) — Default panel and card borders.
- **Strong Brass Border** (#C09A62) — Secondary CTA borders and high-visibility outlines.
- **Dark Panel** (#171410) — Brand strip and final CTA section.
- **Dark Panel Core** (#1F1A14) — Inner surface for dark call-to-action blocks.

Use only the Brass Accent as the accent color. No purple, neon blue, saturated gradients, or glow-heavy effects.

## 3. Typography Rules

- **Display:** Satoshi, Geist, Cabinet Grotesk, or a similarly refined grotesk. Large headings use tight tracking and controlled line-height around 0.94-1.04.
- **Body:** Same sans family, regular/medium weights, relaxed line-height around 1.65-1.8. Body copy should stay under 65 characters per line when possible.
- **Mono:** JetBrains Mono for prices, small badges, key metrics, shortcut labels, and technical specs.
- **Vietnamese Copy:** Keep labels concise and natural. Prefer "Vào cửa hàng" over literal English translations. Avoid stiff product jargon when a simpler Vietnamese phrase reads better.
- **Banned:** Inter as the primary premium font, generic serif fonts, excessive negative tracking, huge text inside compact panels, and all AI-marketing clichés.

## 4. Component Stylings

### Header / Toolbar

The header is a fixed floating toolbar with a warm paper surface and strong tactile controls.

- Height should feel substantial: logo, search, buttons, and cart icon use 56px controls on desktop.
- Logo mark: 56px rounded square, charcoal fill, white text.
- Search input: 56px tall, rounded 1.25rem, visible border (#CDBDA7), left search icon, large readable placeholder.
- Primary navigation: rounded text links with generous padding and hover paper surface.
- Cart icon: use a clean shopping cart icon rather than a shopping bag. Stroke should be light to medium, not heavy.
- Mobile menu remains a clear rounded button with warm brass surface.

### Buttons

- **Primary Button:** Charcoal fill (#171410), white text, 56px height minimum, rounded 1.2rem+, active scale 0.98.
- **Secondary Button:** Brass Surface (#EFE1C7), Strong Brass Border (#C09A62), Deep Cocoa text. Never use low-contrast white outline buttons for primary actions.
- **Dark Section Secondary:** Use dark cocoa fill (#2D261D) with brass border and warm text.
- **Commerce CTAs:** Use Vietnamese action labels: "Vào cửa hàng", "Xem sản phẩm", "Thêm vào giỏ", "Liên hệ hỗ trợ".
- **Banned:** "Compare", "Compare products", washed-out white buttons with invisible borders, neon glows, and vague "Learn more" links.

### Cards

- Use double-bezel construction: outer rounded shell with border and padding, inner core with its own background.
- Major card radius: 2rem to 2.5rem.
- Shadows are warm and subtle, tinted to brown/charcoal, never harsh black.
- Product cards show image, category, title, badge, price, specs, and one clear add-to-cart action.
- Product cards should not include a compare button.

### Section Headers

Each section header should be inside a framed warm paper panel.

- Outer shell: rounded 2.25rem, warm border, paper background, subtle shadow.
- Inner core: milk surface, rounded 1.9rem, generous padding.
- Eyebrow: small mono pill with Brass Accent text.
- Title: large display text under eyebrow.
- Supporting copy and action button must sit directly below the title, aligned left, not pushed far to the right.
- CTA buttons inside section headers should be prominent, at least 64px tall when used as a major action.

### Brand Strip

The brand strip appears after the Buying Architecture section, not directly below the hero.

- Dark charcoal outer strip.
- Warm paper brand tiles.
- Tiles animate in a staggered floating loop using transform and opacity only.
- Animation should feel subtle and premium, not playful or bouncy.

### Tabs

Product tabs are large tactile pills.

- Minimum 48px mobile height, 56px desktop height.
- Horizontal scroll is allowed for the tab rail on small screens, but the page itself must not overflow.
- Active tab uses paper fill and a subtle warm shadow.
- Labels are Vietnamese: "Nổi bật", "Âm thanh", "Máy trạm", "Phụ kiện".

### Inputs

- Use clear visible borders and warm focus rings.
- Place labels or context outside when needed; do not use floating labels.
- Placeholder examples should be Vietnamese, such as "Tìm tai nghe, dock, bàn phím".

## 5. Layout Principles

- Use near-full-width page composition. Desktop page padding should be restrained, around 20-24px.
- Avoid narrow centered content that makes the website feel small.
- Hero is asymmetric: text on the left, large carousel/media on the right.
- Brand Strip appears after the narrative Buying Architecture section.
- Product discovery uses an asymmetric bento grid, not equal three-card rows.
- Buying Architecture uses a sticky framed narrative panel plus media-rich proof cards.
- All multi-column layouts collapse to single-column below 768px.
- No overlapping text, no absolute-positioned text stacks, no incoherent layer collisions.
- Maintain clear scroll rhythm: hero, bento discovery, product shelves, buying architecture, brand proof strip, studio proof, final CTA, footer.

## 6. Motion & Interaction

- Reveal elements with staggered fade-up and subtle scale from 0.985 to 1.
- Use custom cubic-bezier curves: cubic-bezier(0.32, 0.72, 0, 1) for interactive transitions.
- Brand tiles float in a staggered loop using transform and opacity only.
- Buttons use tactile active scale 0.98.
- Product and media cards can scale images slightly on hover.
- Avoid animating top, left, width, or height.
- Respect reduced motion: disable brand tile loop and reveal transforms when users prefer reduced motion.

## 7. Content & IA Rules

- Default interface language is Vietnamese.
- Navigation categories: Laptop, Âm thanh, Bàn phím, Màn hình, Dock, Điện thoại.
- Product tabs: Nổi bật, Âm thanh, Máy trạm, Phụ kiện.
- Primary commerce actions: Vào cửa hàng, Xem sản phẩm, Xem thiết bị, Thêm vào giỏ.
- Support language: Giao hàng, Đổi trả, Bảo hành, Hỗ trợ setup, Liên hệ hỗ trợ.
- The site must not present itself as a comparison engine. It is a curated shop.

## 8. Anti-Patterns (Banned)

- No Compare buttons or compare-first purchase flows.
- No English UI copy unless it is a product/brand name.
- No pure black (#000000).
- No neon purple/blue gradients.
- No outer glow effects.
- No generic three-equal-card feature rows.
- No centered generic hero.
- No thin white outline buttons that disappear into the background.
- No tiny toolbar controls on desktop.
- No heavy icon strokes.
- No AI copywriting clichés such as "Elevate", "Unleash", "Next-Gen", or "Seamless".
- No emojis.
- No custom cursors.
- No broken image URLs.
- No text overlap or mobile horizontal page overflow.
