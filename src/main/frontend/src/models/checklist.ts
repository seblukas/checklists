export default interface Checklist {
  id: string
  name: string
  items: ChecklistItem[]
}
interface ChecklistItem {
  description: string
  checked: boolean
}
