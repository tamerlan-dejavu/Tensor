import { useRef } from 'react'
import { useCipher } from './hooks/useCipher'

export default function App() {
  const {
    inputText, setInputText,
    key, setKey,
    outputText,
    isLoading,
    status, setStatus,
    run,
  } = useCipher()

  const fileInputRef = useRef<HTMLInputElement>(null)
  const charCount = inputText.length

  function handleCopy() {
    if (!outputText) { setStatus({ text: 'Nothing to copy.', ok: false }); return }
    navigator.clipboard.writeText(outputText)
      .then(() => setStatus({ text: 'Copied to clipboard.', ok: true }))
      .catch(() => setStatus({ text: 'Copy failed.', ok: false }))
  }

  function handleSave() {
    if (!outputText) { setStatus({ text: 'Nothing to save.', ok: false }); return }
    const blob = new Blob([outputText], { type: 'text/plain;charset=utf-8' })
    const url = URL.createObjectURL(blob)
    const a = document.createElement('a')
    a.href = url; a.download = 'tensor-cipher-output.txt'
    document.body.appendChild(a); a.click()
    document.body.removeChild(a); URL.revokeObjectURL(url)
    setStatus({ text: 'Output saved.', ok: true })
  }

  function handleFile(e: React.ChangeEvent<HTMLInputElement>) {
    const file = e.target.files?.[0]
    if (!file) return
    const reader = new FileReader()
    reader.onload = (ev) => {
      const text = ev.target?.result as string
      setInputText(text)
      setStatus({ text: `Loaded: ${file.name}`, ok: true })
    }
    reader.onerror = () => setStatus({ text: 'Failed to read file.', ok: false })
    reader.readAsText(file)
    e.target.value = ''
  }

  const inputStyle: React.CSSProperties = {
    width: '100%',
    background: 'var(--bg)',
    border: '1px solid var(--border)',
    color: 'var(--text)',
    padding: '0.6rem 0.85rem',
    fontSize: '0.78rem',
    fontFamily: "'JetBrains Mono', monospace",
    outline: 'none',
    appearance: 'none' as const,
    transition: 'border-color 0.12s ease',
  }

  const labelStyle: React.CSSProperties = {
    display: 'block',
    fontSize: '8.5px',
    fontWeight: 700,
    color: 'var(--text-subtle)',
    letterSpacing: '0.16em',
    textTransform: 'uppercase' as const,
    marginBottom: '0.45rem',
    userSelect: 'none' as const,
  }

  const btnBase: React.CSSProperties = {
    fontFamily: "'JetBrains Mono', monospace",
    fontSize: '9px',
    fontWeight: 700,
    letterSpacing: '0.12em',
    textTransform: 'uppercase' as const,
    cursor: isLoading ? 'not-allowed' : 'pointer',
    display: 'inline-flex',
    alignItems: 'center',
    justifyContent: 'center',
    padding: '0.65rem 1.25rem',
    borderRadius: 0,
    transition: 'background 0.12s ease, color 0.12s ease, border-color 0.12s ease',
    flex: 1,
    minWidth: '120px',
    opacity: isLoading ? 0.38 : 1,
    border: 'none',
  }

  return (
    <div style={{ background: 'var(--bg)', minHeight: '100vh' }}>
      <div style={{ maxWidth: 760, margin: '0 auto', padding: '2rem 1.5rem 7rem' }}>

        {/* Header */}
        <header style={{ textAlign: 'center', marginBottom: '1.5rem', paddingBottom: '1.5rem', borderBottom: '1px solid var(--border)' }}>
          <h1 style={{
            fontFamily: "'Playfair Display', Georgia, serif",
            fontSize: 'clamp(2.2rem, 6vw, 3.4rem)',
            fontWeight: 700,
            color: 'var(--text)',
            letterSpacing: '-0.03em',
            lineHeight: 1.08,
            marginBottom: '1.25rem',
            marginTop: 0,
          }}>
            Tensor-Cube Cipher
          </h1>
          <p style={{ fontSize: '0.72rem', color: 'var(--text-subtle)', letterSpacing: '0.06em', lineHeight: 2.2, margin: 0 }}>
            <span style={{ color: 'var(--text-mid)' }}>4×4×4 tensor blocks</span>
            {' · '}SHA-256 key derivation{' · '}layer shifting + XOR mask
          </p>
        </header>

        {/* Main Section */}
        <div style={{ border: '1px solid var(--border)', padding: '2rem', marginBottom: '1.25rem', background: 'var(--surface)' }}>

          {/* Input Text */}
          <div style={{ marginBottom: '1.25rem' }}>
            <label style={labelStyle}>Input Text</label>
            <textarea
              rows={4}
              value={inputText}
              onChange={e => setInputText(e.target.value)}
              placeholder="Enter plaintext to encrypt or Base64 ciphertext to decrypt..."
              style={{ ...inputStyle, resize: 'vertical' }}
              onFocus={e => (e.target.style.borderColor = 'var(--border-mid)')}
              onBlur={e => (e.target.style.borderColor = 'var(--border)')}
            />
            <div style={{ textAlign: 'right', fontSize: '8.5px', color: 'var(--text-subtle)', marginTop: '0.3rem', letterSpacing: '0.06em' }}>
              {charCount} characters
            </div>
          </div>

          {/* Secret Key */}
          <div style={{ marginBottom: '1.25rem' }}>
            <label style={labelStyle}>Secret Key</label>
            <input
              type="text"
              value={key}
              onChange={e => setKey(e.target.value)}
              placeholder="Enter a secret key for encryption"
              style={inputStyle}
              onFocus={e => (e.target.style.borderColor = 'var(--border-mid)')}
              onBlur={e => (e.target.style.borderColor = 'var(--border)')}
            />
          </div>

          {/* Encrypt / Decrypt */}
          <div style={{ display: 'flex', flexWrap: 'wrap', gap: '0.5rem', marginBottom: '0.625rem' }}>
            <button
              disabled={isLoading}
              onClick={() => run('encrypt')}
              style={{ ...btnBase, background: 'var(--text)', color: 'var(--bg)', border: '1px solid var(--text)' }}
              onMouseEnter={e => { if (!isLoading) (e.currentTarget.style.background = 'var(--text-mid)') }}
              onMouseLeave={e => { if (!isLoading) (e.currentTarget.style.background = 'var(--text)') }}
            >
              {isLoading
                ? <><span className="tc-spinner" />Working...</>
                : 'Encrypt'}
            </button>
            <button
              disabled={isLoading}
              onClick={() => run('decrypt')}
              style={{ ...btnBase, background: 'transparent', color: 'var(--text)', border: '1px solid var(--border-mid)' }}
              onMouseEnter={e => { if (!isLoading) { e.currentTarget.style.background = 'var(--surface-alt)'; e.currentTarget.style.borderColor = 'var(--text-mid)' } }}
              onMouseLeave={e => { if (!isLoading) { e.currentTarget.style.background = 'transparent'; e.currentTarget.style.borderColor = 'var(--border-mid)' } }}
            >
              {isLoading
                ? <><span className="tc-spinner" style={{ borderColor: 'rgba(42,42,42,0.18)', borderTopColor: 'var(--text)' }} />Working...</>
                : 'Decrypt'}
            </button>
          </div>

          {/* File / Save / Copy */}
          <div style={{ display: 'flex', flexWrap: 'wrap', gap: '0.5rem', marginBottom: '1.25rem' }}>
            {(['Load File', 'Save Output', 'Copy'] as const).map(label => (
              <button
                key={label}
                onClick={label === 'Copy' ? handleCopy : label === 'Save Output' ? handleSave : () => fileInputRef.current?.click()}
                style={{
                  ...btnBase,
                  minWidth: '100px',
                  background: 'transparent',
                  color: 'var(--text-mid)',
                  border: '1px solid var(--border)',
                  opacity: 1,
                  cursor: 'pointer',
                }}
                onMouseEnter={e => { e.currentTarget.style.background = 'var(--surface-alt)'; e.currentTarget.style.color = 'var(--text)'; e.currentTarget.style.borderColor = 'var(--border-mid)' }}
                onMouseLeave={e => { e.currentTarget.style.background = 'transparent'; e.currentTarget.style.color = 'var(--text-mid)'; e.currentTarget.style.borderColor = 'var(--border)' }}
              >
                {label}
              </button>
            ))}
            <input ref={fileInputRef} type="file" accept=".txt" onChange={handleFile} style={{ display: 'none' }} />
          </div>

          {/* Output */}
          <div style={{ marginBottom: '1.25rem' }}>
            <label style={labelStyle}>Output</label>
            <textarea
              rows={4}
              readOnly
              value={outputText}
              placeholder="Result will appear here..."
              style={{ ...inputStyle, resize: 'vertical', opacity: outputText ? 1 : 0.65 }}
            />
          </div>

          {/* Status Bar */}
          <div style={{
            textAlign: 'center',
            fontSize: '9.5px',
            padding: '0.625rem 1rem',
            border: '1px solid var(--border)',
            background: 'var(--bg)',
            letterSpacing: '0.06em',
          }}>
            {status
              ? <span className={status.ok ? 'status-success' : 'status-error'}>{status.text}</span>
              : <span style={{ color: 'var(--text-subtle)' }}>Ready.</span>
            }
          </div>
        </div>

        {/* Footer */}
        <footer style={{
          textAlign: 'center',
          fontSize: '8.5px',
          color: 'var(--text-subtle)',
          letterSpacing: '0.14em',
          textTransform: 'uppercase',
          marginTop: '3rem',
          paddingTop: '2rem',
          borderTop: '1px solid var(--border)',
          fontFamily: "'JetBrains Mono', monospace",
        }}>
          Bauyrzhan Tamerlan · Derevyanchenko Kirill
        </footer>

      </div>
    </div>
  )
}
