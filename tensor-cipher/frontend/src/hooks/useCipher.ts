import { useState } from 'react'
import * as api from '../api/cipherApi'

export interface StatusMessage {
  text: string
  ok: boolean
}

export function useCipher() {
  const [inputText, setInputText] = useState('')
  const [key, setKey] = useState('')
  const [outputText, setOutputText] = useState('')
  const [isLoading, setIsLoading] = useState(false)
  const [status, setStatus] = useState<StatusMessage | null>(null)

  async function run(mode: 'encrypt' | 'decrypt') {
    if (!key.trim()) {
      setStatus({ text: 'Secret key is required.', ok: false })
      return
    }
    setIsLoading(true)
    setStatus(null)
    setOutputText('')
    try {
      const fn = mode === 'encrypt' ? api.encrypt : api.decrypt
      const response = await fn(inputText, key)
      setOutputText(response.result)
      setStatus({ text: `Done. Processed in ${response.processingTimeMs} ms.`, ok: true })
    } catch (err) {
      setStatus({ text: err instanceof Error ? err.message : 'Unknown error', ok: false })
    } finally {
      setIsLoading(false)
    }
  }

  return {
    inputText, setInputText,
    key, setKey,
    outputText, setOutputText,
    isLoading,
    status, setStatus,
    run,
  }
}
