import { User } from "@/payload-types";
import { BeforeChangeHook } from "payload/dist/collections/config/types";
import { v4 as uuidv4 } from 'uuid';

const addUser: BeforeChangeHook = ({ req, data }) => {
  const user = req.user as User | null
  return { ...data, user: user?.id }
}

const renameFile: BeforeChangeHook = ({ data }) => {
  const originalFilename = data.filename || '111.png'
  const extension = originalFilename.split('.').pop();
  data.filename = `${uuidv4()}.${extension}`;
  return { ...data }
}

export { addUser, renameFile }