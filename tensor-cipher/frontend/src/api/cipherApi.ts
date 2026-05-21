import axios from 'axios'
import type { CipherResponse } from '../types/cipher'

const BASE_URL = import.meta.env.VITE_API_URL ?? 'http://localhost:8080'

const client = axios.create({ baseURL: BASE_URL })

export async function encrypt(text: string, key: string): Promise<CipherResponse> {
  try {
    const { data } = await client.post<CipherResponse>('/api/cipher/encrypt', { text, key })
    return data
  } catch (err) {
    throw toReadableError(err, 'Encryption')
  }
}

export async function decrypt(text: string, key: string): Promise<CipherResponse> {
  try {
    const { data } = await client.post<CipherResponse>('/api/cipher/decrypt', { text, key })
    return data
  } catch (err) {
    throw toReadableError(err, 'Decryption')
  }
}

function toReadableError(err: unknown, operation: string): Error {
  if (axios.isAxiosError(err)) {
    const msg = err.response?.data?.message ?? err.response?.data?.error ?? err.message
    return new Error(`${operation} failed: ${msg}`)
  }
  return new Error(`${operation} failed: unknown error`)
}
